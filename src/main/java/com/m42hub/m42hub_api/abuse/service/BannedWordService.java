package com.m42hub.m42hub_api.abuse.service;

import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.repository.BannedWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BannedWordService {

    private final BannedWordRepository bannedWordRepository;

    @Transactional(readOnly = true)
    public Optional<BannedWord> findBannedWord(String word) {
        return bannedWordRepository.findByWord(word);
    }

    @Transactional
    public void saveBannedWord(BannedWord bannedWord) {
        bannedWordRepository.save(bannedWord);
    }
}