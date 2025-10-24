package com.m42hub.m42hub_api.abuse.mapper;

import java.time.LocalDateTime;

import com.m42hub.m42hub_api.abuse.dto.request.AbuseRequest;
import com.m42hub.m42hub_api.abuse.dto.response.AbuseResponse;
import com.m42hub.m42hub_api.abuse.entity.Abuse;

public class AbuseMapper {

    public static Abuse toAbuse(AbuseRequest request) {
        return Abuse.builder()
                .targetType(request.targetType())
                .targetId(request.targetId())
                .reasonText(request.reasonText())
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
                abuse.getReasonCategory() != null ? abuse.getReasonCategory().getLabel() : null,
                abuse.getReasonText(),
                abuse.getStatus() != null ? abuse.getStatus().getLabel() : null,
                abuse.getCreatedAt(),
                abuse.getResolvedAt());
    }
}