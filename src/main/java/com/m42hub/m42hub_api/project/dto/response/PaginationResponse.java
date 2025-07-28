package com.m42hub.m42hub_api.project.dto.response;

import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import lombok.Builder;

@Builder
public record PaginationResponse(
        int currentPage,
        int totalPages,
        long totalElements
) {
}
