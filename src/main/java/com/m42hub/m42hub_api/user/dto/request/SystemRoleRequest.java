package com.m42hub.m42hub_api.user.dto.request;


import com.m42hub.m42hub_api.user.entity.Permission;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record SystemRoleRequest(@NotBlank(message = "Nome do cargo é obrigatório") String name,
                                String description,
                                List<Long> permissions
                                ) {
}