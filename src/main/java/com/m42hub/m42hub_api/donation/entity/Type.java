package com.m42hub.m42hub_api.donation.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "DonationType")
@Table(name = "donation_types")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_type_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "hex_color", length = 9)
    private String hexColor;
}