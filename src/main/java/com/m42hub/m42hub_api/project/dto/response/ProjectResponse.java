package com.m42hub.m42hub_api.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
public record ProjectResponse(
        UUID id,
        String name,
        String summary,
        String description,
        StatusResponse status,
        ComplexityResponse complexity,
        String imageUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime creationDate,
        Date startDate,
        Date endDate,
        List<ToolResponse> tools,
        List<TopicResponse> topics,
        List<RoleResponse> unfilledRoles,
        List<MemberResponse> members,
        String discord,
        String github,
        String projectWebsite

) {
}