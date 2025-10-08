package com.m42hub.m42hub_api.user.controller;

import com.m42hub.m42hub_api.user.dto.request.ChangePermissionsRequest;
import com.m42hub.m42hub_api.user.dto.request.SystemRoleRequest;
import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.mapper.SystemRoleMapper;
import com.m42hub.m42hub_api.user.service.SystemRolePermissionService;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/system-role")
@RequiredArgsConstructor
public class SystemRoleController {

    private final SystemRoleService systemRoleService;
    private final SystemRolePermissionService systemRolePermissionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system_role:get_all')")
    public ResponseEntity<List<SystemRoleResponse>> getAll() {
        List<SystemRole> systemRoles = systemRoleService.findAll();

        List<UUID> systemRoleIds = systemRoles.stream().map(SystemRole::getId).toList();
        Map<UUID, List<Permission>> permissionsByRole = systemRolePermissionService
                .findPermissionsBySystemRoleIds(systemRoleIds);

        List<SystemRoleResponse> responses = systemRoles.stream()
                .map(role -> {
                    List<Permission> permissions = permissionsByRole.getOrDefault(role.getId(), List.of());
                    return SystemRoleMapper.toSystemRoleResponse(role, permissions);
                }).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system_role:get_by_id')")
    public ResponseEntity<SystemRoleResponse> getById(@PathVariable UUID id) {
        Optional<SystemRole> existingSystemRoleOpt = systemRoleService.findById(id);

        if (existingSystemRoleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SystemRole systemRole = existingSystemRoleOpt.get();
        List<Permission> permissions = systemRolePermissionService.findPermissionsBySystemRole(systemRole.getId());
        SystemRoleResponse response = SystemRoleMapper.toSystemRoleResponse(systemRole, permissions);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system_role:create')")
    public ResponseEntity<SystemRoleResponse> save(@RequestBody SystemRoleRequest request) {
        SystemRole newSystemRole = SystemRoleMapper.toSystemRole(request);
        SystemRole savedSystemRole = systemRoleService.save(newSystemRole);
        // Usar addRelation ((Singular) Aqui seria uma má ideia, pois em um forEach por exemplo, cada transação seria individual.)
        // addRelations != addRelation (Sem o 'S')
        systemRolePermissionService.addRelations(savedSystemRole.getId(), request.permissions());
        List<Permission> existingPermissions = systemRolePermissionService.findPermissionsBySystemRole(savedSystemRole.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(SystemRoleMapper.toSystemRoleResponse(savedSystemRole, existingPermissions));
    }

    @PatchMapping("/permissions/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:change_unfilled_roles')")
    public ResponseEntity<SystemRoleResponse> changePermissions(@PathVariable UUID id, @RequestBody ChangePermissionsRequest request) {
        Optional<SystemRole> updatedSystemRoleOpt = systemRolePermissionService.updatePermissions(id, request.permissions());

        if (updatedSystemRoleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SystemRole updatedSystemRole = updatedSystemRoleOpt.get();
        List<Permission> updatedPermissions = systemRolePermissionService.findPermissionsBySystemRole(id);
        SystemRoleResponse response = SystemRoleMapper.toSystemRoleResponse(updatedSystemRole, updatedPermissions);

        return ResponseEntity.ok(response);
    }
}