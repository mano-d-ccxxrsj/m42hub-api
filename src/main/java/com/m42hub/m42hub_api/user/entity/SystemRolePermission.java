package com.m42hub.m42hub_api.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "system_role_permissions")
public class SystemRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_role_permissions_id")
    private Long id;

    @Column(name = "system_role_id", nullable = false)
    private UUID systemRoleId;

    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;
}