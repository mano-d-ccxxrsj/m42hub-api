package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

@Builder
public record PermissionResponse(Long id, String name, String description) {
}
