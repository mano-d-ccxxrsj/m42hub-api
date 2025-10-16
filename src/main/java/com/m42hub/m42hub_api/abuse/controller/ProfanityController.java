package com.m42hub.m42hub_api.abuse.controller;

import com.m42hub.m42hub_api.abuse.dto.request.CreateBannedWord;
import com.m42hub.m42hub_api.abuse.dto.request.UpdateStatusBannedWord;
import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.service.BannedWordService;
import com.m42hub.m42hub_api.abuse.service.ProfanityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profanity")
public class ProfanityController {

    private final BannedWordService bannedWordService;
    private final ProfanityService profanityService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:create')")
    public ResponseEntity<Void> saveBannedWord(@RequestBody CreateBannedWord bannedWord) {
        for (String word : bannedWord.words()) {
            if (word == null || word.trim().isEmpty()) continue;

            String trimmed = word.trim();
            String normalized = profanityService.compress(profanityService.normalize(trimmed));

            BannedWord existing = bannedWordService.findBannedWord(trimmed).orElse(null);
            if (existing == null) {
                BannedWord bw = new BannedWord();
                bw.setWord(trimmed);
                bw.setActive(true);
                bannedWordService.saveBannedWord(bw);
            }

            profanityService.getForbiddenWords().add(normalized);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PostMapping("/status")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:update')")
    public ResponseEntity<Void> updateStatusOfBannedWord(@RequestBody UpdateStatusBannedWord bannedWord) {
        String trimmed = bannedWord.word().trim();

        BannedWord existing = bannedWordService.findBannedWord(trimmed).orElse(null);
        if (existing != null) {
            existing.setActive(bannedWord.isActive());
            bannedWordService.saveBannedWord(existing);

            if (bannedWord.isActive()) {
                profanityService.addToCache(trimmed);
            } else {
                profanityService.removeFromCache(trimmed);
            }
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}