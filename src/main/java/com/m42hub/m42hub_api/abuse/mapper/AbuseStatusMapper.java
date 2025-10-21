package com.m42hub.m42hub_api.abuse.mapper;

import com.m42hub.m42hub_api.abuse.dto.response.AbuseStatusResponse;
import com.m42hub.m42hub_api.abuse.entity.AbuseStatus;

public class AbuseStatusMapper {

    public static AbuseStatusResponse toAbuseStatusResponse(AbuseStatus abuseStatus) {
        return new AbuseStatusResponse(
                abuseStatus.getId(),
                abuseStatus.getName(),
                abuseStatus.getDescription());
    }
}