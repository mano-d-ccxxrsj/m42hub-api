package com.m42hub.m42hub_api;

import com.m42hub.m42hub_api.config.CustomPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;

@EnableFeignClients
@SpringBootApplication
public class M42hubApiApplication {
	private static final Logger logger = LoggerFactory.getLogger(M42hubApiApplication.class);

	public static void main(String[] args) {
		// Lê o .env e os armazena como propriedades do Spring
		// Das quais podem e são usadas com o @Value posteriormente pelo código.
		CustomPropertiesLoader ignored = new CustomPropertiesLoader();

		SpringApplication app = new SpringApplication(M42hubApiApplication.class);
		ConfigurableApplicationContext context = app.run(args);

		Environment env = context.getEnvironment();
		if (isProdProfileActive(env)) {
			logger.info("Você está em modo de desenvolvimento!");
		}
	}

	private static boolean isProdProfileActive(Environment env) {
		return !List.of(env.getActiveProfiles()).contains("prod");
	}
}