package com.m42hub.m42hub_api.user.dto.request;


import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChangePermissionsRequest(
        @NotBlank(message = "permissions é obrigatório")
        List<Long> permissions
) {
}