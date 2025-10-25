package com.m42hub.m42hub_api.abuse.service;

import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.entity.UserFlag;
import com.m42hub.m42hub_api.abuse.repository.BannedWordRepository;
import com.m42hub.m42hub_api.config.ProfanityFilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ProfanityService {

    private final BannedWordRepository bannedWordRepository;
    private final UserFlagService userFlagService;
    private Set<String> forbiddenWordsCache;

    @Autowired
    public ProfanityService(
            BannedWordRepository bannedWordRepository,
            UserFlagService  userFlagService
    ) {
        this.bannedWordRepository = bannedWordRepository;
        this.userFlagService = userFlagService;
        this.reloadForbiddenWords();
    }

    public void reloadForbiddenWords() {
        List<BannedWord> list = bannedWordRepository.findByIsActiveTrue();
        Set<String> normalized = new HashSet<>();
        for (BannedWord bannedWord : list) {
            normalized.add(this.normalizeAndCompressWord(bannedWord.getWord()));
        }
        forbiddenWordsCache = normalized;
    }

    public void addToCache(String word) {
        forbiddenWordsCache.add(this.normalizeAndCompressWord(word));
    }

    public Set<String> getForbiddenWords() {
        return new HashSet<>(forbiddenWordsCache);
    }

    public void removeFromCache(String word) {
        forbiddenWordsCache.remove(this.normalizeAndCompressWord(word));
    }

    public String normalizeAndCompressWord(String word) {
        return ProfanityFilterConfig.compress(ProfanityFilterConfig.normalize(word));
    }

    public String normalizeWord(String word) {
        return ProfanityFilterConfig.normalize(word);
    }





    public void validateField(UserFlag userFlag) {
        String text = userFlag.getAttemptedText();
        if (text == null || text.trim().isEmpty()) return;

        List<String> matched = new ArrayList<>();
        matched.addAll(this.findForbiddenWordsInText(text));
        matched.addAll(this.findForbiddenSequencesInText(text, matched));

        if (!matched.isEmpty()) {
            userFlagService.saveUserFlag(userFlag);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ação não permitida pois viola nossas regras de conduta");
        }
    }

    private List<String> findForbiddenWordsInText(String text) {
        List<String> matched = new ArrayList<>();
        String[] words = ProfanityFilterConfig.splitIntoWords(text);
        for (String word : words) {
            String normalizedWord = this.normalizeAndCompressWord(word.trim());
            if (forbiddenWordsCache.contains(normalizedWord) && !matched.contains(normalizedWord)) {
                matched.add(normalizedWord);
            }
        }
        return matched;
    }

    private List<String> findForbiddenSequencesInText(String text, List<String> alreadyMatched) {
        List<String> matched = new ArrayList<>();

        String normalizedAndCompressedText = this.normalizeAndCompressWord(text);
        String continuousText = ProfanityFilterConfig.removeSpaces(normalizedAndCompressedText);

        for (String forbidden : forbiddenWordsCache) {
            if (!alreadyMatched.contains(forbidden)) {
                if (normalizedAndCompressedText.contains(forbidden) || continuousText.contains(forbidden)) {
                    matched.add(forbidden);
                }
            }
        }
        return matched;
    }
}