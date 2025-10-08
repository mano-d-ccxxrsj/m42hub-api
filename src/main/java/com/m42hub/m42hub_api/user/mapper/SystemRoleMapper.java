package com.m42hub.m42hub_api.user.mapper;

import com.m42hub.m42hub_api.user.dto.request.SystemRoleRequest;
import com.m42hub.m42hub_api.user.dto.response.PermissionResponse;
import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SystemRoleMapper {

    public static SystemRole toSystemRole(SystemRoleRequest request) {
        return SystemRole
                .builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static SystemRoleResponse toSystemRoleResponse(SystemRole systemRole, List<Permission> permissions) {
        List<PermissionResponse> permissionResponses = permissions.stream()
                .map(PermissionMapper::toPermissionResponse)
                .toList();

        return SystemRoleResponse.builder()
                .id(systemRole.getId())
                .name(systemRole.getName())
                .description(systemRole.getDescription())
                .permissions(permissionResponses)
                .build();
    }
}