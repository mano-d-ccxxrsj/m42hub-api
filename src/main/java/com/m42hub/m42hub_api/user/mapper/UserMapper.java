package com.m42hub.m42hub_api.user.mapper;

import com.m42hub.m42hub_api.user.dto.request.UserRequest;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toUser(UserRequest request) {

        SystemRole systemRole = SystemRole.builder().id(2L).build();

        return User
                .builder()
                .username(request.username())
                .password(request.password())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .profilePicUrl(request.profilePicUrl())
                .isActive(request.isActive())
                .systemRole(systemRole)
                .build();
    }

    public static UserResponse toUserResponse(User user) {

        SystemRoleResponse systemRole = SystemRoleMapper.toSystemRoleResponse(user.getSystemRole());

        return UserResponse
                .builder()
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

    public static AuthenticatedUserResponse toAuthenticatedUserResponse(User user) {

        Long roleId = null;
        String roleName = null;
        if (user.getSystemRole() != null) {
            roleId = user.getSystemRole().getId();
            roleName = user.getSystemRole().getName();
        }

        return AuthenticatedUserResponse
                .builder()
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roleId(roleId)
                .roleName(roleName)
                .build();
    }

}
