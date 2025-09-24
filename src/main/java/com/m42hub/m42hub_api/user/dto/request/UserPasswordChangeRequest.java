package com.m42hub.m42hub_api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordChangeRequest(
        @NotBlank(message = "Senha atual é obrigatória")
        @Size(min = 6, message = "Senha atual deve ter no mínimo 6 caracteres")
        String oldPassword,

        @NotBlank(message = "Nova senha é obrigatória")
        @Size(min = 6, message = "Nova senha deve ter no mínimo 6 caracteres")
        String newPassword
) {
}