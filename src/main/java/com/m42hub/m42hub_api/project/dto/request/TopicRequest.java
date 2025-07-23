package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TopicRequest(@NotBlank(message = "Nome da tecnologia/ferramenta é obrigatório") String name,
                           String description,
                           @Size(max = 9, message = "Cor em hexadecimal deve ter no máximo 9 caracteres") String hexColor) {
}