package com.m42hub.m42hub_api.abuse.service;

import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.repository.BannedWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BannedWordService {

    private final BannedWordRepository bannedWordRepository;
    private final ProfanityService profanityService;

    @Transactional(readOnly = true)
    public List<BannedWord> getBannedWords() {
        List<BannedWord> wordList = new ArrayList<>();
        for (BannedWord bannedWord : bannedWordRepository.findAllByOrderByWordAsc()) {
            // Caso o cadastro das palavras tenha sido feito de forma censurada: Curse != C
            bannedWord.setWord(profanityService.normalizeWord(bannedWord.getWord()));
            wordList.add(bannedWord);
        }
        return wordList;
    }

    @Transactional
    public boolean processBannedWords(List<String> words) {
        List<String> validTrimmedWords = words.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .distinct()
                .toList();

        if (validTrimmedWords.isEmpty()) return false;

        List<BannedWord> allExistingWords = bannedWordRepository.findAllByOrderByWordAsc();
        Map<String, BannedWord> normalizedToEntity = new HashMap<>();

        for (BannedWord existingWord : allExistingWords) {
            String normalized = profanityService.normalizeAndCompressWord(existingWord.getWord());
            normalizedToEntity.put(normalized, existingWord);
        }

        List<BannedWord> wordsToSave = new ArrayList<>();
        Set<String> normalizedToAddToCache = new HashSet<>();

        for (String word : validTrimmedWords) {
            String normalized = profanityService.normalizeAndCompressWord(word);

            BannedWord existingWord = normalizedToEntity.get(normalized);
            if (existingWord != null) {
                if (!existingWord.isActive()) {
                    existingWord.setActive(true);
                    wordsToSave.add(existingWord);
                    normalizedToAddToCache.add(normalized);
                }
            } else {
                BannedWord newWord = new BannedWord();
                newWord.setWord(word);
                newWord.setActive(true);
                wordsToSave.add(newWord);
                normalizedToAddToCache.add(normalized);
                normalizedToEntity.put(normalized, newWord);
            }
        }

        if (!wordsToSave.isEmpty()) {
            bannedWordRepository.saveAll(wordsToSave);
        }

        for (String normalized : normalizedToAddToCache) {
            profanityService.addToCache(normalized);
        }

        return true;
    }

    @Transactional
    public boolean updateBannedWordStatus(String word, boolean isActive) {
        String trimmed = word.trim();
        String normalized = profanityService.normalizeAndCompressWord(trimmed);

        List<BannedWord> allWords = bannedWordRepository.findAllByOrderByWordAsc();
        Optional<BannedWord> targetWord = allWords.stream()
                .filter(bannedWord -> {
                    String bannedWordNormalized = profanityService.normalizeAndCompressWord(bannedWord.getWord());
                    return bannedWordNormalized.equals(normalized);
                })
                .findFirst();

        targetWord.ifPresent(foundWord -> {
            foundWord.setActive(isActive);
            bannedWordRepository.save(foundWord);

            if (isActive) {
                profanityService.addToCache(normalized);
            } else {
                profanityService.removeFromCache(normalized);
            }
        });

        return true;
    }
}