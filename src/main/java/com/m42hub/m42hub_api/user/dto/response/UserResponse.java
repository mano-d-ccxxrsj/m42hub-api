package com.m42hub.m42hub_api.user.dto.response;

import com.m42hub.m42hub_api.user.entity.SystemRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponse(
        Long id,
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
