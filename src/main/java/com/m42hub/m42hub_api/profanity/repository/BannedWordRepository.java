package com.m42hub.m42hub_api.profanity.repository;

import com.m42hub.m42hub_api.profanity.entity.BannedWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BannedWordRepository extends JpaRepository<BannedWord, UUID> {
    List<BannedWord> findByActiveTrue();
}