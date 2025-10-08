package com.m42hub.m42hub_api.config;

import lombok.Builder;

import java.util.UUID;

@Builder
public record JWTUserData(UUID id, String username, UUID role) {
}