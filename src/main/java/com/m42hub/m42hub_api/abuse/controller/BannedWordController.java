package com.m42hub.m42hub_api.abuse.controller;

import com.m42hub.m42hub_api.abuse.dto.request.CreateBannedWord;
import com.m42hub.m42hub_api.abuse.dto.request.UpdateStatusBannedWord;
import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import com.m42hub.m42hub_api.abuse.service.BannedWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profanity")
public class BannedWordController {

    private final BannedWordService bannedWordService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('permission:get_all')")
    public ResponseEntity<List<BannedWord>> getAllBannedWords() {
        List<BannedWord> bannedWordList = bannedWordService.getBannedWords();
        return ResponseEntity.status(HttpStatus.OK).body(bannedWordList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:create')")
    public ResponseEntity<Void> saveBannedWord(@RequestBody CreateBannedWord bannedWord) {
        bannedWordService.processBannedWords(bannedWord.words());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PatchMapping("/status")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:update')")
    public ResponseEntity<Void> updateStatusOfBannedWord(@RequestBody UpdateStatusBannedWord bannedWord) {
        bannedWordService.updateBannedWordStatus(bannedWord.word(), bannedWord.isActive());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}