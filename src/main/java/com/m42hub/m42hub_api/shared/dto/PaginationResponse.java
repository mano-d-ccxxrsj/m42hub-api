package com.m42hub.m42hub_api.shared.dto;

import lombok.Builder;

@Builder
public record PaginationResponse(
        int currentPage,
        int totalPages,
        long totalElements
) {
}
