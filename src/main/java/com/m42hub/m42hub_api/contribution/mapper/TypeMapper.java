package com.m42hub.m42hub_api.contribution.mapper;

import com.m42hub.m42hub_api.contribution.dto.request.TypeRequest;
import com.m42hub.m42hub_api.contribution.dto.response.TypeResponse;
import com.m42hub.m42hub_api.contribution.entity.Type;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TypeMapper {

    public static Type toType(TypeRequest request) {
        return Type
                .builder()
                .name(request.name())
                .label(request.label())
                .hexColor(request.hexColor())
                .description(request.description())
                .build();
    }

    public static TypeResponse toTypeResponse(Type type) {
        return TypeResponse
                .builder()
                .id(type.getId())
                .name(type.getName())
                .label(type.getLabel())
                .description(type.getDescription())
                .build();
    }

}
