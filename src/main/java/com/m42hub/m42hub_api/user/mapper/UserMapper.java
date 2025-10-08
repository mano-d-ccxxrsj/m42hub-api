package com.m42hub.m42hub_api.user.mapper;

import com.m42hub.m42hub_api.project.dto.response.RoleResponse;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.mapper.RoleMapper;
import com.m42hub.m42hub_api.user.dto.request.UserInfoRequest;
import com.m42hub.m42hub_api.user.dto.request.UserRequest;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import com.m42hub.m42hub_api.user.dto.response.UserInfoResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class UserMapper {

    public static User toUser(UserRequest request) {
        return User
                .builder()
                .username(request.username().toLowerCase())
                .password(request.password())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .isActive(request.isActive())
                .build();
    }

    public static User toUser(UserInfoRequest request) {
        return User
                .builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .biography(request.biography())
                .discord(request.discord())
                .linkedin(request.linkedin())
                .github(request.github())
                .personalWebsite(request.personalWebsite())
                .build();
    }

    public static UserResponse toUserResponse(User user, SystemRoleResponse systemRole) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isActive(user.getIsActive())
                .systemRole(systemRole)
                .lastLogin(user.getLastLogin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static AuthenticatedUserResponse toAuthenticatedUserResponse(User user, SystemRole systemRole) {
        UUID roleId = null;
        String roleName = null;

        if (systemRole != null) {
            roleId = systemRole.getId();
            roleName = systemRole.getName();
        }

        return AuthenticatedUserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePicUrl(user.getProfilePicUrl())
                .profileBannerUrl(user.getProfileBannerUrl())
                .roleId(roleId)
                .roleName(roleName)
                .build();
    }

    public static UserInfoResponse toUserInfoResponse(User user, SystemRole systemRole, List<Role> interestRoles) {
        UUID roleId = null;
        String roleName = null;

        if (systemRole != null) {
            roleId = systemRole.getId();
            roleName = systemRole.getName();
        }

        List<RoleResponse> interestedRoles = new ArrayList<>();
        if (interestRoles != null && !interestRoles.isEmpty()) {
            interestedRoles = interestRoles.stream()
                    .map(RoleMapper::toRoleResponse)
                    .toList();
        }

        return UserInfoResponse.builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePicUrl(user.getProfilePicUrl())
                .profileBannerUrl(user.getProfileBannerUrl())
                .roleId(roleId)
                .roleName(roleName)
                .biography(user.getBiography())
                .discord(user.getDiscord())
                .linkedin(user.getLinkedin())
                .github(user.getGithub())
                .personalWebsite(user.getPersonalWebsite())
                .interestRoles(interestedRoles)
                .build();
    }
}