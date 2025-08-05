package com.m42hub.m42hub_api.project.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberResponse(
        Long id,
        Boolean isManager,
        Long project,
        Long role,
        AuthenticatedUserResponse user,
        MemberStatusResponse memberStatus,
        String applicationMessage,
        LocalDateTime createdAt
) {
}
