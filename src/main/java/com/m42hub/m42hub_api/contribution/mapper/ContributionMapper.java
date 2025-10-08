package com.m42hub.m42hub_api.contribution.mapper;

import com.m42hub.m42hub_api.contribution.dto.request.ContributionRequest;
import com.m42hub.m42hub_api.contribution.dto.response.*;
import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ContributionMapper {

    public static Contribution toContribution(ContributionRequest request) {
        return Contribution.builder()
                .name(request.name())
                .description(request.description())
                .statusId(request.statusId())
                .typeId(request.typeId())
                .submittedAt(request.submittedAt())
                .approvedAt(request.approvedAt())
                .userId(request.userId())
                .build();
    }

    public static ContributionResponse toContributionResponse(
            Contribution contribution,
            StatusResponse status,
            TypeResponse type,
            UserInfoResponse userInfo
    ) {
        return ContributionResponse.builder()
                .userInfo(userInfo)
                .name(contribution.getName())
                .description(contribution.getDescription())
                .status(status)
                .type(type)
                .submittedAt(contribution.getSubmittedAt())
                .approvedAt(contribution.getApprovedAt())
                .build();
    }

    public static ContributionListItemResponse toContributionListItemResponse(
            Contribution contribution,
            String statusName,
            String typeName,
            UserInfoResponse userInfo
    ) {
        return ContributionListItemResponse.builder()
                .id(contribution.getId())
                .name(contribution.getName())
                .description(contribution.getDescription())
                .statusName(statusName)
                .typeName(typeName)
                .submittedAt(contribution.getSubmittedAt())
                .approvedAt(contribution.getApprovedAt())
                .creationDate(contribution.getCreatedAt())
                .userInfo(userInfo)
                .build();
    }

    public static ContributionsByUserResponse toContributionsByUserResponse(
            UserInfoResponse userInfo,
            List<ContributionListItemResponse> contributions
    ) {
        return ContributionsByUserResponse.builder()
                .userInfo(userInfo)
                .contributions(contributions)
                .build();
    }
}