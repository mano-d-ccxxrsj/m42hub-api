package com.m42hub.m42hub_api.donation.mapper;

import com.m42hub.m42hub_api.donation.dto.request.PlatformRequest;
import com.m42hub.m42hub_api.donation.dto.response.PlatformResponse;
import com.m42hub.m42hub_api.donation.entity.Platform;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PlatformMapper {

    public static Platform toPlatform(PlatformRequest request) {
        return Platform
                .builder()
                .name(request.name())
                .label(request.label())
                .hexColor(request.hexColor())
                .description(request.description())
                .build();
    }

    public static PlatformResponse toPlatformResponse(Platform platform) {
        return PlatformResponse
                .builder()
                .id(platform.getId())
                .name(platform.getName())
                .label(platform.getLabel())
                .description(platform.getDescription())
                .build();
    }

}
