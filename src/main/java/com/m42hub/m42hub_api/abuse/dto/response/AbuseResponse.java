package com.m42hub.m42hub_api.abuse.dto.response;

import java.time.LocalDateTime;

public record AbuseResponse(
    Long id,
    Long reporterId,
    String reporterUsername,
    String targetType,
    Long targetId,
    String reasonCategoryName,
    String reasonText,
    String status,
    LocalDateTime createdAt,
    LocalDateTime resolvedAt
) {}