package com.m42hub.m42hub_api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ChangePermissionsRequest(
        @NotEmpty(message = "permissions é obrigatório")
        List<Long> permissions
) {
}