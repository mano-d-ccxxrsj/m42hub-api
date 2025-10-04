package com.m42hub.m42hub_api.contribution.mapper;

import com.m42hub.m42hub_api.contribution.dto.request.StatusRequest;
import com.m42hub.m42hub_api.contribution.dto.response.StatusResponse;
import com.m42hub.m42hub_api.contribution.entity.Status;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatusMapper {

    public static Status toStatus(StatusRequest request) {
        return Status
                .builder()
                .name(request.name())
                .label(request.label())
                .description(request.description())
                .build();
    }

    public static StatusResponse toStatusResponse(Status status) {
        return StatusResponse
                .builder()
                .id(status.getId())
                .name(status.getName())
                .label(status.getLabel())
                .description(status.getDescription())
                .build();
    }

}
