package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record SystemRoleResponse(UUID id, String name, String description, List<PermissionResponse> permissions) {
}