package com.m42hub.m42hub_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CustomPropertiesLoader {

    private static final Logger logger = LoggerFactory.getLogger(CustomPropertiesLoader.class);

    public CustomPropertiesLoader() {
        Properties env = loadEnv();

        expandAllVariables(env);

        String host = env.getProperty("DATABASE_HOST", "localhost");
        String port = env.getProperty("POSTGRES_PORT", "5432");
        String dbName = env.getProperty("POSTGRES_DB", "m42hub");
        String username = env.getProperty("POSTGRES_USER", "postgres");
        String password = env.getProperty("POSTGRES_PASSWORD", "postgres");

        String rawUrl = env.getProperty("DATABASE_URL", "jdbc:postgresql://" + host + ":" + port + "/" + dbName);
        String url = expandVariables(rawUrl, env);

        System.setProperty("spring.datasource.url", url);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);

        String imgBbUrl = env.getProperty("IMGBB_UPLOAD_URL");
        if (imgBbUrl != null) {
            System.setProperty("imgBB.upload-url", imgBbUrl);
        }

        if (!tryConnection(url, username, password)) {
            String fallbackUrl = "jdbc:postgresql://localhost:" + port + "/" + dbName;
            logger.info("Tentando fallback: {}", fallbackUrl);
            System.setProperty("spring.datasource.url", fallbackUrl);

            if (!tryConnection(fallbackUrl, username, password)) {
                throw new RuntimeException("Não foi possível conectar ao banco em nenhuma URL");
            }
        }
    }

    private void expandAllVariables(Properties props) {
        Properties expandedProps = new Properties();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key);
            expandedProps.setProperty(key, value);
        }

        for (String key : expandedProps.stringPropertyNames()) {
            String value = expandedProps.getProperty(key);
            String expandedValue = expandVariables(value, expandedProps);

            System.setProperty(key, expandedValue);
//            logger.info("Definida property: {} = {}", key, expandedValue);
        }
    }

    private String expandVariables(String text, Properties props) {
        if (text == null) return null;

        String result = text;
        for (String key : props.stringPropertyNames()) {
            String placeholder = "${" + key + "}";
            String value = props.getProperty(key);
            if (value != null) {
                result = result.replace(placeholder, value);
            }
        }
        return result;
    }

    private static boolean tryConnection(String url, String username, String password) {
        try (Connection ignored = DriverManager.getConnection(url, username, password)) {
            logger.info("Conexão bem-sucedida!");
            return true;
        } catch (SQLException e) {
            logger.error("Falha ao conectar em: {} - {}", url, e.getMessage());
            return false;
        }
    }

    private static Properties loadEnv() {
        try {
            return loadExampleEnv();
        } catch (IOException e1) {
            logger.info("Falha ao carregar example.env: {}", e1.getMessage());
            try {
                return loadDefaultEnv();
            } catch (IOException e2) {
                logger.error("Falha ao carregar .env: {}", e2.getMessage());
                return new Properties();
            }
        }
    }

    private static Properties loadExampleEnv() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("example.env")) {
            props.load(fis);
            logger.info("Arquivo example.env carregado com sucesso");
        }
        return props;
    }

    private static Properties loadDefaultEnv() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            props.load(fis);
            logger.info("Arquivo .env carregado com sucesso");
        }
        return props;
    }
}