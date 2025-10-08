package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean isActive,
        SystemRoleResponse systemRole,
        LocalDateTime lastLogin,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}