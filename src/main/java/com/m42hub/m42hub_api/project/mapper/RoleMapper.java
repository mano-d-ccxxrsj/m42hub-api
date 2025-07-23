package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.RoleRequest;
import com.m42hub.m42hub_api.project.dto.response.RoleResponse;
import com.m42hub.m42hub_api.project.entity.Role;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoleMapper {

    public static Role toRole(RoleRequest request) {
        return Role
                .builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse
                .builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }

}
