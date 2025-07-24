package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SystemRoleResponse(Long id, String name, String description, List<PermissionResponse> permissions) {
}
