package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

public record StatusRequest(@NotBlank(message = "Nome do status é obrigatório") String name,
                            String description) {
}