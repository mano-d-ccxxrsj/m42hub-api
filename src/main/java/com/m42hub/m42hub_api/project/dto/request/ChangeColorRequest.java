package com.m42hub.m42hub_api.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeColorRequest(
        @NotBlank(message = "Cor em hexadecimal é obrigatória")
        @Size(min = 2, max = 9, message = "Cor em hexadecimal deve ter no máximo 9 caracteres")
        String hexColor) {
}