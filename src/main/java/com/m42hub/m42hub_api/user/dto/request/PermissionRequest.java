package com.m42hub.m42hub_api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionRequest(
        @NotBlank(message = "Nome da permissão é obrigatória")
        @Size(min = 3, message = "Nome da permissão deve ter no mínimo 3 caracteres")
        String name,
        String description
) {
}