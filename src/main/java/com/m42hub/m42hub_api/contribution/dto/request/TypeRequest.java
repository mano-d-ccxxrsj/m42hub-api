package com.m42hub.m42hub_api.contribution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TypeRequest(
        String name,
        @NotBlank(message = "Label do status é obrigatório")
        @Size(min = 3, message = "Label do status deve ter no mínimo 3 caracteres")
        String label,
        String description,
        @Size(max = 9, message = "Cor em hexadecimal deve ter no máximo 9 caracteres")
        String hexColor
) {
}
