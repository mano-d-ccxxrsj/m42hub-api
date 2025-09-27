package com.m42hub.m42hub_api.donation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

public record DonationRequest(
        @NotBlank(message = "Nome da doação é obrigatório")
        @Size(min = 3, message = "Label do status deve ter no mínimo 3 caracteres")
        String name,
        @NotBlank(message = "Resumo da doação é obrigatório")
        @Size(min = 3, message = "Label do status deve ter no mínimo 3 caracteres")
        String summary,
        @NotNull(message = "Usuário que realizou a doação é obrigatório")
        Long userId,
        String description,
        String imageUrl,
        @NotNull(message = "Valor monetário da doação é obrigatório")
        BigDecimal amount,
        String currency,
        Long statusId,
        Long typeId,
        Long platformId,
        @NotNull(message = "Data da doação é obrigatória")
        Date donatedAt
) {
}
