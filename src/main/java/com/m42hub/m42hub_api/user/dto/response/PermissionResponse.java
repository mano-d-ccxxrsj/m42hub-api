package com.m42hub.m42hub_api.user.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PermissionResponse(UUID id, String name, String description) {
}