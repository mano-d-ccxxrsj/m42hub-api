package com.m42hub.m42hub_api.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_unfilled_roles")
public class ProjectUnfilledRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_unfilled_roles_id")
    private Long id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "project_role_id", nullable = false)
    private Long roleId;
}