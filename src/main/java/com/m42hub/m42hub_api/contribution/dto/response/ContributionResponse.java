package com.m42hub.m42hub_api.contribution.dto.response;

import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.Builder;

import java.util.Date;

@Builder
public record ContributionResponse(
        UserInfoResponse userInfo,
        String name,
        String description,
        StatusResponse status,
        TypeResponse type,
        Date submittedAt,
        Date approvedAt
) {
}
