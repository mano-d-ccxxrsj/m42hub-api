package com.m42hub.m42hub_api.contribution.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
public record ContributionListItemResponse(
        UUID id,
        String name,
        String description,
        String statusName,
        String typeName,
        Date submittedAt,
        Date approvedAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime creationDate,
        UserInfoResponse userInfo
) {
}
