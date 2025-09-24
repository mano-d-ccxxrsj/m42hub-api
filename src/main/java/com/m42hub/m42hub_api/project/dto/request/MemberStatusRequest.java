package com.m42hub.m42hub_api.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberStatusRequest(
        @NotBlank(message = "Nome do status é obrigatório")
        @Size(min = 3, message = "Nome do status deve ter no mínimo 3 caracteres")
        String name,
        String description
) {
}