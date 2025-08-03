package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

public record ChangeUnfilledRolesRequest(
        @NotBlank(message = "UnfilledRoles é obrigatório")
        List<Long> unfilledRoles
) {
}