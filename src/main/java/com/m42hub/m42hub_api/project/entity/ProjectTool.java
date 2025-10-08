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
@Table(name = "projects_tools")
public class ProjectTool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "projects_tools_id")
    private Long id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @Column(name = "project_tools_id", nullable = false)
    private Long toolId;
}