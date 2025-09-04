package com.m42hub.m42hub_api.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberProjectResponse(
        Long id,
        Boolean isManager,
        ProjectListItemResponse projectListItem,
        Long roleId,
        AuthenticatedUserResponse user,
        MemberStatusResponse memberStatus,
        String applicationMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime createdAt
) {
}
