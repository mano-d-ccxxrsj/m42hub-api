package com.m42hub.m42hub_api.project.dto.response;

import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long id,
        Boolean isManager,
        Long project,
        Long role,
        AuthenticatedUserResponse user,
        MemberStatusResponse memberStatus,
        String applicationMessage
) {
}
