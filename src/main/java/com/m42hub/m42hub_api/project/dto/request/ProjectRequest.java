package com.m42hub.m42hub_api.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import java.util.List;

public record ProjectRequest(
        @NotBlank(message = "Nome do projeto é obrigatório")
        @Size(min = 3, message = "Nome do projeto deve ter no mínimo 3 caracteres")
        String name,
        @NotBlank(message = "Resumo do projeto é obrigatório")
        @Size(min = 10, message = "Resumo do projeto ter no mínimo 10 caracteres")
        String summary,
        @NotBlank(message = "Descrição do projeto é obrigatório")
        @Size(min = 10, message = "Descrição do projeto deve ter no mínimo 10 caracteres")
        String description,
        Long statusId,
        @NotNull(message = "Complexidade do projeto é obrigatória")
        Long complexityId,
        String imageUrl,
        @NotNull(message = "Data prevista de incio do projeto é obrigatória")
        Date startDate,
        Date endDate,
        @NotEmpty(message = "É necessário informar ao menos uma Ferramenta/Tecnologia")
        List<Long> toolIds,
        @NotEmpty(message = "É necessário informar ao menos um Assunto/Tópico")
        List<Long> topicIds,
        @NotEmpty(message = "É necessário informar ao cargo a ser preenchido")
        List<Long> unfilledRoleIds,
        @NotNull(message = "É necessário informar o cargo do manager")
        Long managerRoleId,
        String discord,
        String github,
        String projectWebsite
) {
}