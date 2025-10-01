package com.m42hub.m42hub_api.shared.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PageResponse<T>(
        List<T> content,
        PaginationResponse pagination
) {
}
