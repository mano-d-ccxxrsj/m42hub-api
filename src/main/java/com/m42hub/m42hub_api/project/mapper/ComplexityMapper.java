package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.ComplexityRequest;
import com.m42hub.m42hub_api.project.dto.response.ComplexityResponse;
import com.m42hub.m42hub_api.project.entity.Complexity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ComplexityMapper {

    public static Complexity toComplexity(ComplexityRequest request) {
        return Complexity
                .builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static ComplexityResponse toComplexityResponse(Complexity complexity) {
        return ComplexityResponse
                .builder()
                .id(complexity.getId())
                .name(complexity.getName())
                .description(complexity.getDescription())
                .build();
    }

}
