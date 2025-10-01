package com.m42hub.m42hub_api.shared.mapper;

import com.m42hub.m42hub_api.shared.dto.PageResponse;
import com.m42hub.m42hub_api.shared.dto.PaginationResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@UtilityClass
public class PageMapper {
    public static <T, R> PageResponse<R> toPagedResponse(Page<T> page, Function<T, R> converter) {
        List<R> content = page.getContent().stream().map(converter).toList();

        PaginationResponse pagination = new PaginationResponse(
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements()
        );

        return new PageResponse<>(content, pagination);
    }
}
