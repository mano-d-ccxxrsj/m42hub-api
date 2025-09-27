package com.m42hub.m42hub_api.donation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StatusRequest(
        String name,
        @NotBlank(message = "Label do status é obrigatório")
        @Size(min = 3, message = "Label do status deve ter no mínimo 3 caracteres")
        String label,
        String description
) {
}
