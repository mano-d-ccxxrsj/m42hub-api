package com.m42hub.m42hub_api.donation.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "donationStatus")
@Table(name = "donation_statuses")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_status_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(length = 100, nullable = false)
    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;
}
