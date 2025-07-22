package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

public record StatusRequest(@NotBlank(message = "Nome da tecnologia/ferramenta é obrigatório") String name,
                            String description) {
}