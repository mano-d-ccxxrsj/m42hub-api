package com.m42hub.m42hub_api.abuse.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_flags")
public class UserFlag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long userId;

    private String field;

    private String action;

    @Column(columnDefinition = "TEXT")
    private String targetEndpoint;

    @Column(columnDefinition = "TEXT")
    private String attemptedText;

    @Column(columnDefinition = "TEXT")
    private String matchedWords;

    @Column(columnDefinition = "TEXT")
    private String details;

    @CreationTimestamp
    private LocalDateTime createdAt;
}