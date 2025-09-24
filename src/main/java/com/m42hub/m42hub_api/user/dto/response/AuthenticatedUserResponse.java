package com.m42hub.m42hub_api.user.dto.response;

import com.m42hub.m42hub_api.project.dto.response.RoleResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record AuthenticatedUserResponse(
        Long id,
        String username,
        String firstName,
        String lastName,
        String profilePicUrl,
        String profileBannerUrl,
        Long roleId,
        String roleName
) {
}
