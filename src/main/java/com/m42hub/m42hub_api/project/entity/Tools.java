package com.m42hub.m42hub_api.project.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project_tools")
public class Tools {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_tools_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "hex_color", length = 9)
    private String hexColor;

    @Column(columnDefinition = "TEXT")
    private String description;
}
