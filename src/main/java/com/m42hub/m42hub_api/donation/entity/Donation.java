package com.m42hub.m42hub_api.donation.entity;

import com.m42hub.m42hub_api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "donation_id")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(precision = 12, scale = 4, nullable = false)
    private BigDecimal amount;

    @Column(length = 3, nullable = false)
    private String currency = "BRL";

    @ManyToOne()
    @JoinColumn(name = "status_id", referencedColumnName = "donation_status_id", nullable = false)
    private Status status;

    @ManyToOne()
    @JoinColumn(name = "type_id", referencedColumnName = "donation_type_id", nullable = false)
    private Type type;

    @ManyToOne()
    @JoinColumn(name = "platform_id", referencedColumnName = "donation_platform_id", nullable = false)
    private Platform platform;

    @Column(name="donated_at", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date donatedAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;
}
