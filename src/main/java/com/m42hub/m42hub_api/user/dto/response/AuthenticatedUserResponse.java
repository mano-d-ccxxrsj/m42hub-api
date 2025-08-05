package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuthenticatedUserResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        Long roleId,
        String roleName
) {
}
