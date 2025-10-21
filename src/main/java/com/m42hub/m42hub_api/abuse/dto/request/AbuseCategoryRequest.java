package com.m42hub.m42hub_api.abuse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AbuseCategoryRequest(
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
    String name,
    
    @NotBlank(message = "Label é obrigatória")
    @Size(max = 100, message = "Label deve ter no máximo 100 caracteres")
    String label,
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    String description
) {}