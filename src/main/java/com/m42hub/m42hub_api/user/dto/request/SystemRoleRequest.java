package com.m42hub.m42hub_api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SystemRoleRequest(
        @NotBlank(message = "Nome do cargo é obrigatório")
        @Size(min = 3, message = "Nome do cargo deve ter no mínimo 3 caracteres")
        String name,
        String description,
        List<Long> permissions
) {
}