package com.m42hub.m42hub_api.project.dto.response;

import com.m42hub.m42hub_api.project.entity.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

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
        Date startDate,
        Date endDate,
        List<ToolResponse> tools,
        List<TopicResponse> topics,
        List<RoleResponse> unfilledRoles,
        List<MemberResponse> members
) {
}
