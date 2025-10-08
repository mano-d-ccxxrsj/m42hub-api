package com.m42hub.m42hub_api.contribution.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contributions")
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "contribution_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String link;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date submittedAt;

    @Temporal(TemporalType.DATE)
    private Date approvedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}