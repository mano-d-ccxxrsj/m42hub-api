package com.m42hub.m42hub_api.donation.mapper;

import com.m42hub.m42hub_api.donation.dto.request.StatusRequest;
import com.m42hub.m42hub_api.donation.dto.response.StatusResponse;
import com.m42hub.m42hub_api.donation.entity.Status;
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
