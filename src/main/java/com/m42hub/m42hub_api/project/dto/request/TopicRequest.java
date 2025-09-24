package com.m42hub.m42hub_api.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicRequest(
        @NotBlank(message = "Nome do tópico/assunto é obrigatório")
        @Size(min = 3, message = "Nome do tópico/assunto deve ter no mínimo 3 caracteres")
        String name,
        String description,
        @NotBlank(message = "Cor em hexadecimal é obrigatória")
        @Size(max = 9, message = "Cor em hexadecimal deve ter no máximo 9 caracteres")
        String hexColor
) {
}