package com.m42hub.m42hub_api.project.dto.response;

import lombok.Builder;

@Builder
public record ToolResponse(Long id, String name, String description, String hexColor) {
}
