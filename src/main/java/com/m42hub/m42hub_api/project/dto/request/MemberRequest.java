package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank(message = "Projeto é obrigatório")
        Long projectId,
        @NotBlank(message = "Cargo é obrigatório")
        Long roleId,
        @NotBlank(message = "Usuário é obrigatório")
        Long userId,
        String applicationMessage
) {
}