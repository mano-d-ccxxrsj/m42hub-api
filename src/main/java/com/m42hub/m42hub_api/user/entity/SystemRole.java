package com.m42hub.m42hub_api.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "system_roles")
public class SystemRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "system_role_id")
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
}