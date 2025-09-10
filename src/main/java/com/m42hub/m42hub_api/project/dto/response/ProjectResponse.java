package com.m42hub.m42hub_api.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m42hub.m42hub_api.project.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Builder
public record ProjectResponse(
        Long id,
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
