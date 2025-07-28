package com.m42hub.m42hub_api.project.dto.response;

import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> content,
        PaginationResponse pagination
) {
}
