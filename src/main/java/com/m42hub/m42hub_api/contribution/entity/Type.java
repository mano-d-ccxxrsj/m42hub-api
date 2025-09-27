package com.m42hub.m42hub_api.contribution.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contribution_types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contribution_type_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String label;

    @Column(name = "hex_color", length = 9)
    private String hexColor;

    @Column(columnDefinition = "TEXT")
    private String description;
}
