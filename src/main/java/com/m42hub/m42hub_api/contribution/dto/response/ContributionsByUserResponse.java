package com.m42hub.m42hub_api.contribution.dto.response;

import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record ContributionsByUserResponse(
        UserInfoResponse userInfo,
        List<ContributionListItemResponse> contributions
) {
}