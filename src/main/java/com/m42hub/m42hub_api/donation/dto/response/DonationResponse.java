package com.m42hub.m42hub_api.donation.dto.response;

import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record DonationResponse(
        String name,
        String summary,
        UserInfoResponse userInfo,
        String description,
        String imageUrl,
        BigDecimal amount,
        String currency,
        StatusResponse status,
        TypeResponse type,
        PlatformResponse platform,
        Date donatedAt
) {
}
