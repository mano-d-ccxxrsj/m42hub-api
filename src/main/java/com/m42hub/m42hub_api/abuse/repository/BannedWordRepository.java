package com.m42hub.m42hub_api.abuse.repository;

import com.m42hub.m42hub_api.abuse.entity.BannedWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BannedWordRepository extends JpaRepository<BannedWord, UUID> {
    List<BannedWord> findByIsActiveTrue();
    List<BannedWord> findAllByOrderByWordAsc();
    Optional<BannedWord> findByWord(String word);
}