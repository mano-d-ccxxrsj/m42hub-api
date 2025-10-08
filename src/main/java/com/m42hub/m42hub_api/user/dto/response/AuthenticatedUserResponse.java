package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AuthenticatedUserResponse(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String profilePicUrl,
        String profileBannerUrl,
        UUID roleId,
        String roleName
) {
}