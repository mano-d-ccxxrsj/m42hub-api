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

        List<Permission> permissions = request.permissions().stream()
                .map(permissionId -> Permission.builder().id(permissionId).build())
                .toList();

        return SystemRole
                .builder()
                .name(request.name())
                .description(request.description())
                .permissions(permissions)
                .build();
    }

    public static SystemRoleResponse toSystemRoleResponse(SystemRole systemRole) {

        List<PermissionResponse> permissions = systemRole.getPermissions().stream()
                .map(PermissionMapper::toPermissionResponse)
                .toList();

        return SystemRoleResponse
                .builder()
                .id(systemRole.getId())
                .name(systemRole.getName())
                .description(systemRole.getDescription())
                .permissions(permissions)
                .build();
    }

}
