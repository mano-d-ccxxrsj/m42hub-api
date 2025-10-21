package com.m42hub.m42hub_api.abuse.mapper;

import com.m42hub.m42hub_api.abuse.dto.request.AbuseCategoryRequest;
import com.m42hub.m42hub_api.abuse.dto.response.AbuseCategoryResponse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AbuseCategoryMapper {

    public AbuseCategoryResponse toAbuseCategoryResponse(AbuseCategory entity) {
        return new AbuseCategoryResponse(entity.getId(), entity.getName(), entity.getLabel(), entity.getDescription());
    }

    public AbuseCategory toAbuseCategory(AbuseCategoryRequest request) {
        return AbuseCategory.builder()
                .name(request.name())
                .label(request.label())
                .description(request.description())
                .build();
    }
}
