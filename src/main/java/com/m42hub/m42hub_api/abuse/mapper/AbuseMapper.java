package com.m42hub.m42hub_api.abuse.mapper;

import java.time.LocalDateTime;

import com.m42hub.m42hub_api.abuse.dto.request.AbuseRequest;
import com.m42hub.m42hub_api.abuse.dto.response.AbuseResponse;
import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.enums.AbuseStatusEnum;
import com.m42hub.m42hub_api.user.entity.User;

public class AbuseMapper {

    public static Abuse toAbuse(AbuseRequest request, User reporter, AbuseCategory category) {
        return Abuse.builder()
                .reporter(reporter)
                .targetType(request.targetType())
                .targetId(request.targetId())
                .reasonCategory(category)
                .reasonText(request.reasonText())
                .status(AbuseStatusEnum.OPEN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static AbuseResponse toAbuseResponse(Abuse abuse) {
        return new AbuseResponse(
                abuse.getId(),
                abuse.getReporter().getId(),
                abuse.getReporter().getUsername(),
                abuse.getTargetType().getDisplayName(),
                abuse.getTargetId(),
                abuse.getReasonCategory() != null ? abuse.getReasonCategory().getName() : null,
                abuse.getReasonText(),
                abuse.getStatus().getDisplayName(),
                abuse.getCreatedAt(),
                abuse.getResolvedAt());
    }
}