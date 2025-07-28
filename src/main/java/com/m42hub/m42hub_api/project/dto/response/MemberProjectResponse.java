package com.m42hub.m42hub_api.project.dto.response;

import lombok.Builder;

@Builder
public record MemberProjectResponse(
        Long id,
        Boolean isManager,
        RoleResponse role
) {
}
