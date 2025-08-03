package com.m42hub.m42hub_api.project.dto.request;


import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

public record ProjectUpdateRequest(
        String name,
        String summary,
        String description,
        Long statusId,
        Long complexityId,
        String imageUrl,
        Date startDate,
        Date endDate,
        List<Long> toolIds,
        List<Long> topicIds,
        List<Long> unfilledRoleIds
) {
}