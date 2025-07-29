package com.m42hub.m42hub_api.project.dto.response;

import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberResponse(
        Long id,
        Boolean isManager,
        Long project,
        Long role,
        Long user
) {
}
