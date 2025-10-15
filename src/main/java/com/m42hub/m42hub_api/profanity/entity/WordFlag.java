package com.m42hub.m42hub_api.profanity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "word_flags")
public class WordFlag {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    private String field;

    private String action;

    @Column(columnDefinition = "TEXT")
    private String attemptedText;

    @Column(columnDefinition = "TEXT")
    private String matchedWords;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt = LocalDateTime.now();
}