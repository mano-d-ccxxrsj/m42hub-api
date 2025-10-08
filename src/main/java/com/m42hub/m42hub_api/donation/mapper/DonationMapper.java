package com.m42hub.m42hub_api.donation.mapper;

import com.m42hub.m42hub_api.donation.dto.request.DonationRequest;
import com.m42hub.m42hub_api.donation.dto.response.*;
import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DonationMapper {

    public static Donation toDonation(DonationRequest request) {
        return Donation.builder()
                .userId(request.userId())
                .name(request.name())
                .summary(request.summary())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .amount(request.amount())
                .currency(request.currency())
                .statusId(request.statusId())
                .typeId(request.typeId())
                .platformId(request.platformId())
                .donatedAt(request.donatedAt())
                .build();
    }

    public static DonationResponse toDonationResponse(
            Donation donation,
            StatusResponse status,
            TypeResponse type,
            PlatformResponse platform,
            UserInfoResponse userInfo
    ) {
        return DonationResponse.builder()
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

    public static DonationListItemResponse toDonationListItemResponse(
            Donation donation,
            String statusName,
            String typeName,
            String platformName,
            UserInfoResponse userInfo
    ) {
        return DonationListItemResponse.builder()
                .id(donation.getId())
                .name(donation.getName())
                .summary(donation.getSummary())
                .description(donation.getDescription())
                .statusName(statusName)
                .typeName(typeName)
                .platformName(platformName)
                .donatedAt(donation.getDonatedAt())
                .creationDate(donation.getCreatedAt())
                .userInfo(userInfo)
                .build();
    }
}