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
        @NotBlank(message = "É necessário informar ao menos uma Ferramenta/Tecnologia")
        List<Long> toolIds,
        @NotBlank(message = "É necessário informar ao menos um Assunto/Tópico")
        List<Long> topicIds,
        @NotBlank(message = "É necessário informar ao cargo a ser preenchido")
        List<Long> unfilledRoleIds,
        @NotBlank(message = "É necessário informar o cargo do manager")
        Long managerRoleId,
        String discord,
        String github,
        String projectWebsite

) {
}