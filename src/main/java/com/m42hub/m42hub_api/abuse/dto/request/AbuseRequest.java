package com.m42hub.m42hub_api.abuse.dto.request;

import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AbuseRequest(
    @NotNull(message = "Tipo do alvo é obrigatório") 
    TargetTypeAbuseEnum targetType,
    
    @NotNull(message = "ID do alvo é obrigatório") 
    Long targetId,
    
    @NotNull(message = "Categoria da denúncia é obrigatória") 
    Long reasonCategoryId,
    
    @NotBlank(message = "Texto do motivo é obrigatório") 
    String reasonText
) {}