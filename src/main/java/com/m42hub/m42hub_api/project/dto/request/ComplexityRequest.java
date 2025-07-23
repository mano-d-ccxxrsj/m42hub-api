package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

public record ComplexityRequest(@NotBlank(message = "Nome da complexidade é obrigatório") String name,
                                String description) {
}