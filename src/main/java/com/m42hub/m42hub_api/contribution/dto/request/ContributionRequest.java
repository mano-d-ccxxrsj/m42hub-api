package com.m42hub.m42hub_api.contribution.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record ContributionRequest(
        @NotBlank(message = "Label do status é obrigatório")
        @Size(min = 3, message = "Label do status deve ter no mínimo 3 caracteres")
        String name,
        @NotBlank(message = "Usuário que realizou a contribuição é obrigatório")
        Long userId,
        String description,
        Long statusId,
        Long typeId,
        @NotNull(message = "Data de submissão da contribuição é obrigatória")
        Date submittedAt,
        Date approvedAt
) {
}
