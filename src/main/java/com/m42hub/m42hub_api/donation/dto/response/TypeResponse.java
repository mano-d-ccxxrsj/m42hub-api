package com.m42hub.m42hub_api.donation.dto.response;

import lombok.Builder;

@Builder
public record TypeResponse(Long id, String name, String label, String hexColor, String description) {
}
