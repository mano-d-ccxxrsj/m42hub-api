package com.m42hub.m42hub_api.abuse.entity;

import java.time.LocalDateTime;

import com.m42hub.m42hub_api.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "abuses")
public class Abuse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "abuse_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", referencedColumnName = "user_id", nullable = false)
    private User reporter;

    @Column(name = "target_type", length = 50, nullable = false)
    private String targetType; // TODO trocar esse cara para um ENUM 

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reason_category_id", referencedColumnName = "abuse_category_id")
    private AbuseCategory reasonCategory;

    @Column(name = "reason_text", columnDefinition = "TEXT", nullable = false)
    private String reasonText;

    @Column(name = "status", length = 20, nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}