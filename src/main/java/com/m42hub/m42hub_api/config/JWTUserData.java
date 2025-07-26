package com.m42hub.m42hub_api.config;


import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import lombok.Builder;

@Builder
public record JWTUserData(Long id, String username, String role) {
}