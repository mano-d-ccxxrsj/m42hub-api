package com.m42hub.m42hub_api.donation.mapper;

import com.m42hub.m42hub_api.donation.dto.request.DonationRequest;
import com.m42hub.m42hub_api.donation.dto.response.*;
import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DonationMapper {

    public static Donation toDonation(DonationRequest request) {

        Status status = Status.builder().id(request.statusId()).build();
        Type type = Type.builder().id(request.typeId()).build();
        Platform platform = Platform.builder().id(request.platformId()).build();
        User user = User.builder().id(request.userId()).build();

        return Donation
                .builder()
                .user(user)
                .name(request.name())
                .summary(request.summary())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .amount(request.amount())
                .currency(request.currency())
                .status(status)
                .type(type)
                .platform(platform)
                .donatedAt(request.donatedAt())
                .build();
    }

    public static DonationResponse toDonationResponse(Donation donation) {

        StatusResponse status = donation.getStatus() != null ? StatusMapper.toStatusResponse(donation.getStatus()) : null;
        TypeResponse type = donation.getType() != null ? TypeMapper.toTypeResponse(donation.getType()) : null;
        PlatformResponse platform = donation.getPlatform() != null ? PlatformMapper.toPlatformResponse(donation.getPlatform()) : null;
        UserInfoResponse userInfo = donation.getUser() != null ? UserMapper.toUserInfoResponse(donation.getUser()) : null;

        return DonationResponse
                .builder()
                .userInfo(userInfo)
                .name(donation.getName())
                .summary(donation.getSummary())
                .description(donation.getDescription())
                .imageUrl(donation.getImageUrl())
                .amount(donation.getAmount())
                .currency(donation.getCurrency())
                .status(status)
                .type(type)
                .platform(platform)
                .donatedAt(donation.getDonatedAt())
                .build();
    }

    public static DonationListItemResponse toDonationListItemResponse(Donation donation) {

        String statusName = donation.getStatus() != null ? donation.getStatus().getName() : null;
        String typeName = donation.getType() != null ? donation.getType().getName() : null;
        String platformName = donation.getPlatform() != null ? donation.getPlatform().getName() : null;
        UserInfoResponse userInfoResponse = donation.getUser() != null ? UserMapper.toUserInfoResponse(donation.getUser()) : null;

        return DonationListItemResponse
                .builder()
                .id(donation.getId())
                .name(donation.getName())
                .summary(donation.getSummary())
                .description(donation.getDescription())
                .statusName(statusName)
                .typeName(typeName)
                .platformName(platformName)
                .donatedAt(donation.getDonatedAt())
                .creationDate(donation.getCreatedAt())
                .userInfo(userInfoResponse)
                .build();
    }
}
