package com.m42hub.m42hub_api.user.mapper;

import com.m42hub.m42hub_api.user.dto.request.PermissionRequest;
import com.m42hub.m42hub_api.user.dto.response.PermissionResponse;
import com.m42hub.m42hub_api.user.entity.Permission;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PermissionMapper {

    public static Permission toPermission(PermissionRequest request) {
        return Permission
                .builder()
                .name(request.name())
                .description(request.description())
                .build();
    }

    public static PermissionResponse toPermissionResponse(Permission permission) {
        return PermissionResponse
                .builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

}
