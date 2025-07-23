package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

public record RoleRequest(@NotBlank(message = "Nome do cargo é obrigatório") String name,
                          String description) {
}