package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.MemberStatusRequest;
import com.m42hub.m42hub_api.project.dto.request.StatusRequest;
import com.m42hub.m42hub_api.project.dto.response.MemberStatusResponse;
import com.m42hub.m42hub_api.project.dto.response.StatusResponse;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.entity.Status;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberStatusMapper {

    public static MemberStatus toMemberStatus(MemberStatusRequest request) {
        return MemberStatus
                .builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static MemberStatusResponse toMemberStatusResponse(MemberStatus memberStatus) {
        return MemberStatusResponse
                .builder()
                .id(memberStatus.getId())
                .name(memberStatus.getName())
                .description(memberStatus.getDescription())
                .build();
    }

}
