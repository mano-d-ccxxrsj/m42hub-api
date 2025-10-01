package com.m42hub.m42hub_api.donation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Builder
public record DonationListItemResponse(
        UUID id,
        String name,
        String summary,
        String description,
        String statusName,
        String typeName,
        String platformName,
        Date donatedAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime creationDate,
        UserInfoResponse userInfo
) {
}
