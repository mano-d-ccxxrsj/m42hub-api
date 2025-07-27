package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

public record ProjectRequest(
        @NotBlank(message = "Nome do projeto é obrigatório")
        String name,
        @NotBlank(message = "Resumo do projeto é obrigatório")
        String summary,
        @NotBlank(message = "Descrição do projeto é obrigatória")
        String description,
        Long statusId,
        @NotBlank(message = "Complexidade do projeto é obrigatória")
        Long complexityId,
        String imageUrl,
        @NotBlank(message = "Data prevista de incio do projeto é obrigatória")
        Date startDate,
        Date endDate,
        List<Long> tools,
        List<Long> topics,
        List<Long> unfilledRoles,
        List<Long> members
) {
}