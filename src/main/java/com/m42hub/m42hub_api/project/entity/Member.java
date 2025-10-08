package com.m42hub.m42hub_api.project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_members_id")
    private Long id;

    @Column(name = "is_manager", nullable = false)
    private Boolean isManager;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "project_role_id", nullable = false)
    private Long roleId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @Column(name = "application_message")
    private String applicationMessage;

    @Column(name = "application_feedback")
    private String applicationFeedback;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}