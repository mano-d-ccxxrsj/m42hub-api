package com.m42hub.m42hub_api.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.m42hub.m42hub_api.abuse.entity.UserFlag;
import com.m42hub.m42hub_api.abuse.service.ProfanityService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class ProfanityFilterConfig {

    private final ProfanityService profanityService;

    // Esclarecimento, esta função/method está a ser criada a partir de dados de uma requisição.
    private static UserFlag createUserFlag(String endpoint, String textValue, String fieldName, String method, Long userId) {
        return UserFlag.builder()
                .targetEndpoint(endpoint)
                .attemptedText(textValue)
                .field(fieldName)
                .action(method)
                .userId(userId)
                .build();
    }

    public void validate(String jsonPayload, Long userId, String method, String endpoint) {
        if (jsonPayload == null || jsonPayload.trim().isEmpty()) return;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonPayload);

            Iterator<String> fieldNames = jsonNode.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                JsonNode value = jsonNode.get(fieldName);

                if (value.isTextual()) {
                    String textValue = value.asText();

                    UserFlag userFlag = createUserFlag(endpoint, textValue, fieldName, method, userId);

                    profanityService.validateField(userFlag);
                } else if (value.isArray()) {
                    for (JsonNode element : value) {
                        if (element.isTextual()) {
                            String textValue = element.asText();

                            UserFlag userFlag = createUserFlag(endpoint, textValue, fieldName, method, userId);

                            profanityService.validateField(userFlag);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Payload JSON inválido");
        }
    }

    public static String removeSpaces(String text) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char charAt = text.charAt(i);
            if (!Character.isWhitespace(charAt)) {
                stringBuilder.append(charAt);
            }
        }
        return stringBuilder.toString();
    }

    public static String[] splitIntoWords(String text) {
        if (text == null || text.isEmpty()) return new String[0];
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isWordSeparator(c)) {
                if (!currentWord.isEmpty()) {
                    words.add(currentWord.toString());
                    currentWord = new StringBuilder();
                }
            } else {
                currentWord.append(c);
            }
        }
        if (!currentWord.isEmpty()) words.add(currentWord.toString());
        return words.toArray(new String[0]);
    }

    public static boolean isWordSeparator(char character) {
        return character == ' ' || character == '\t' || character == '\n' || character == '\r';
    }

    public static String normalize(String word) {
        if (word == null || word.isEmpty()) return "";

        Map<Character, Character> mapOfCharacters = Map.ofEntries(
                Map.entry('0', 'o'),
                Map.entry('1', 'i'),
                Map.entry('2', 's'),
                Map.entry('3', 'e'),
                Map.entry('4', 'a'),
                Map.entry('5', 's'),
                Map.entry('6', 'g'),
                Map.entry('7', 'l'),
                Map.entry('8', 'b'),
                Map.entry('9', 'g'),
                Map.entry('@', 'a'),
                Map.entry('#', 'h'),
                Map.entry('$', 's'),
                Map.entry('!', 'i'),
                Map.entry('|', 'i'),
                Map.entry('q', 'o'),
                Map.entry('ü', 'u'), // no inglês ü -> you TODO: Contexto de símbolos atribuídos como significados para palavras. É bom analisar sobre.
                Map.entry('-', ' '),
                Map.entry('_', ' '),
                Map.entry('\\', ' '),
                Map.entry('/', ' ')
        );

        StringBuilder stringBuilder = new StringBuilder();
        String lower = word.toLowerCase();

        for (char character : lower.toCharArray()) {

            if (Character.isLetter(character)) {
                stringBuilder.append(character);
            } else if (mapOfCharacters.containsKey(character)) {
                char replacement = mapOfCharacters.get(character);
                stringBuilder.append(replacement);
            }

        }
        return stringBuilder.toString();
    }

    public static String compress(String word) {
        if (word == null || word.isEmpty()) return word;
        StringBuilder stringBuilder = new StringBuilder();
        char previewCharacter = 0;
        for (char character : word.toCharArray()) {
            if (character != previewCharacter || Character.isWhitespace(character)) {
                stringBuilder.append(character);
            }
            previewCharacter = character;
        }
        return stringBuilder.toString();
    }
}