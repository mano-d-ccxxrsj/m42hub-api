package com.m42hub.m42hub_api.project.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MemberRequest(
        @NotBlank(message = "Projeto é obrigatório")
        UUID projectId,
        @NotBlank(message = "Cargo é obrigatório")
        Long roleId,
        UUID userId,
        String applicationMessage
) {
}