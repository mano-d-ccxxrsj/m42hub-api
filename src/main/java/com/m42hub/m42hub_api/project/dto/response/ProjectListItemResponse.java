package com.m42hub.m42hub_api.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
public record ProjectListItemResponse(
        Long id,
        String name,
        String summary,
        String statusName,
        String complexityName,
        String imageUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime creationDate,
        Date startDate,
        Date endDate,
        List<String> toolNames,
        List<String> topicNames,
        List<String> unfilledRoleNames,
        String manager,
        String discord,
        String github,
        String projectWebsite
) {
}
