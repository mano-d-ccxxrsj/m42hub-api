package com.m42hub.m42hub_api.user.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record ChangePermissionsRequest(
        @NotEmpty(message = "permissions é obrigatório")
        List<UUID> permissions
) {
}