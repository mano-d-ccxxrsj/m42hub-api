package com.m42hub.m42hub_api.user.entity;

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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID id;

    @Column(length = 50, nullable = false)
    private String username;

    private String password;

    private String email;

    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "profile_pic_url", columnDefinition = "TEXT")
    private String profilePicUrl;

    @Column(name = "profile_banner_url", columnDefinition = "TEXT")
    private String profileBannerUrl;

    private String biography;

    private String discord;

    private String linkedin;

    private String github;

    @Column(name = "personal_website")
    private String personalWebsite;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "system_role_id", nullable = false)
    private UUID systemRoleId;
}