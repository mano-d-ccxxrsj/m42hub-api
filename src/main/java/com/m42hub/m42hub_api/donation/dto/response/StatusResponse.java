package com.m42hub.m42hub_api.donation.dto.response;

import lombok.Builder;

@Builder
public record StatusResponse(Long id, String name, String label, String description) {
}
