package com.m42hub.m42hub_api.user.dto.request;


import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "Nome de usuário é obrigatório")
        String username,
        @NotBlank(message = "Senha é obrigatória")
        String password,
        @NotBlank(message = "E-mail é obrigatório")
        String email,
        String profilePicUrl,
        String firstName,
        String lastName,
        Boolean isActive
) {
}