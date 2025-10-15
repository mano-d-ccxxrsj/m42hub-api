package com.m42hub.m42hub_api.profanity.service;

import com.m42hub.m42hub_api.profanity.entity.BannedWord;
import com.m42hub.m42hub_api.profanity.entity.WordFlag;
import com.m42hub.m42hub_api.profanity.repository.BannedWordRepository;
import com.m42hub.m42hub_api.profanity.repository.WordFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ProfanityService {

    private final BannedWordRepository bannedWordRepository;
    private final WordFlagRepository wordFlagRepository;
    private Set<String> forbiddenWords;

    @Autowired
    public ProfanityService(BannedWordRepository bannedWordRepository, WordFlagRepository wordFlagRepository) {
        this.bannedWordRepository = bannedWordRepository;
        this.wordFlagRepository = wordFlagRepository;
        this.reloadForbiddenWords();
    }

    public void reloadForbiddenWords() {
        List<BannedWord> list = bannedWordRepository.findByActiveTrue();
        Set<String> normalized = new HashSet<>();
        for (BannedWord bw : list) {
            normalized.add(this.compress(this.normalize(bw.getWord())));
        }
        forbiddenWords = normalized;
    }

    /**
     * Exemplo de uso:
     * <p></p>
     * <blockquote><pre>
     * profanityService.validate(request.getName(), userId, "name", "create");
     * </pre></blockquote>
     * <p></p>
     * Onde obter o userId você já sabe como:
     * <blockquote><pre>
     * Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     * JWTUserData userData = (JWTUserData) authentication.getPrincipal();
     * </pre></blockquote>
     * <p></p>
     * Ex:
     * <blockquote><pre>
     * userData.id();
     * </pre></blockquote>
     * */
    public void validate(String text, UUID userId, String field, String action) {
        if (text == null || text.trim().isEmpty()) return;

        List<String> matched = new ArrayList<>();
        matched.addAll(this.findForbiddenWordsInText(text));
        matched.addAll(this.findForbiddenSequencesInText(text, matched));

        if (!matched.isEmpty()) {
            this.logFlag(userId, field, action, text, matched);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Palavras proibidas detectadas: " + String.join(", ", matched));
        }
    }

    private List<String> findForbiddenWordsInText(String text) {
        List<String> matched = new ArrayList<>();
        String[] words = this.splitIntoWords(text);
        for (String word : words) {
            String normalizedWord = this.compress(normalize(word.trim()));
            if (forbiddenWords.contains(normalizedWord) && !matched.contains(normalizedWord)) {
                matched.add(normalizedWord);
            }
        }
        return matched;
    }

    private List<String> findForbiddenSequencesInText(String text, List<String> alreadyMatched) {
        List<String> matched = new ArrayList<>();

        String normalizedText = this.normalize(text);
        String compressedText = this.compress(normalizedText);
        String continuousText = this.removeSpaces(compressedText);

        for (String forbidden : forbiddenWords) {
            if (!alreadyMatched.contains(forbidden)) {
                if (compressedText.contains(forbidden) || continuousText.contains(forbidden)) {
                    matched.add(forbidden);
                }
            }
        }
        return matched;
    }

    private String removeSpaces(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void logFlag(UUID userId, String field, String action, String text, List<String> matched) {
        WordFlag flag = new WordFlag();
        flag.setUserId(userId);
        flag.setField(field);
        flag.setAction(action);
        flag.setAttemptedText(text);
        flag.setMatchedWords(String.join(", ", matched));
        wordFlagRepository.save(flag);
    }

    private String[] splitIntoWords(String text) {
        if (text == null || text.isEmpty()) return new String[0];
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (this.isWordSeparator(c)) {
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

    private boolean isWordSeparator(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private String normalize(String word) {
        if (word == null || word.isEmpty()) return "";

        Map<Character, Character> map = Map.ofEntries(
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
                Map.entry('-', ' '),
                Map.entry('_', ' '),
                Map.entry('\\', ' '),
                Map.entry('/', ' ')
        );

        StringBuilder sb = new StringBuilder();
        String lower = word.toLowerCase();

        for (char c : lower.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(c);
            } else if (map.containsKey(c)) {
                char replacement = map.get(c);
                sb.append(replacement);
            }
        }
        return sb.toString();
    }

    private String compress(String word) {
        if (word == null || word.isEmpty()) return word;
        StringBuilder sb = new StringBuilder();
        char prev = 0;
        for (char c : word.toCharArray()) {
            if (c != prev || Character.isWhitespace(c)) {
                sb.append(c);
            }
            prev = c;
        }
        return sb.toString();
    }
}