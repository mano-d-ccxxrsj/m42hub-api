package com.m42hub.m42hub_api.contribution.mapper;

import com.m42hub.m42hub_api.contribution.dto.request.ContributionRequest;
import com.m42hub.m42hub_api.contribution.dto.response.ContributionResponse;
import com.m42hub.m42hub_api.contribution.dto.response.StatusResponse;
import com.m42hub.m42hub_api.contribution.dto.response.TypeResponse;
import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContributionMapper {


    public static Contribution toContribution(ContributionRequest request) {

        Status status = Status.builder().id(request.statusId()).build();
        Type type = Type.builder().id(request.typeId()).build();
        User user = User.builder().id(request.userId()).build();

        return Contribution
                .builder()
                .name(request.name())
                .user(user)
                .description(request.description())
                .status(status)
                .type(type)
                .submittedAt(request.submittedAt())
                .approvedAt(request.approvedAt())
                .build();
    }

    public static ContributionResponse toContributionResponse(Contribution contribution) {

        StatusResponse status = contribution.getStatus() != null ? StatusMapper.toStatusResponse(contribution.getStatus()) : null;
        TypeResponse type = contribution.getType() != null ? TypeMapper.toTypeResponse(contribution.getType()) : null;
        UserInfoResponse userInfo = contribution.getUser() != null ? UserMapper.toUserInfoResponse(contribution.getUser()) : null;

        return ContributionResponse
                .builder()
                .name(contribution.getName())
                .userInfo(userInfo)
                .description(contribution.getDescription())
                .status(status)
                .type(type)
                .submittedAt(contribution.getSubmittedAt())
                .approvedAt(contribution.getApprovedAt())
                .build();
    }

}
