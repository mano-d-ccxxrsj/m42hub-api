package com.m42hub.m42hub_api.profanity.repository;

import com.m42hub.m42hub_api.profanity.entity.WordFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WordFlagRepository extends JpaRepository<WordFlag, UUID> {}