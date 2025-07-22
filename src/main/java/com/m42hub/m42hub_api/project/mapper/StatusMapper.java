package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.StatusRequest;
import com.m42hub.m42hub_api.project.dto.response.StatusResponse;
import com.m42hub.m42hub_api.project.entity.Status;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatusMapper {

    public static Status toStatus(StatusRequest request) {
        return Status
                .builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static StatusResponse toStatusResponse(Status status) {
        return StatusResponse
                .builder()
                .id(status.getId())
                .name(status.getName())
                .description(status.getDescription())
                .build();
    }

}
