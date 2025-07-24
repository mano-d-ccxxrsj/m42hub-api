package com.m42hub.m42hub_api.user.dto.request;


import jakarta.validation.constraints.NotBlank;

public record PermissionRequest(@NotBlank(message = "Nome do cargo é obrigatório") String name,
                                String description) {
}