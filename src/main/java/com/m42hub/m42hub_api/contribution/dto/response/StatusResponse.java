package com.m42hub.m42hub_api.contribution.dto.response;

import lombok.Builder;

@Builder
public record StatusResponse(Long id, String name, String label, String description) {
}
