package com.m42hub.m42hub_api.user.controller;

import com.m42hub.m42hub_api.user.dto.request.PermissionRequest;
import com.m42hub.m42hub_api.user.dto.request.SystemRoleRequest;
import com.m42hub.m42hub_api.user.dto.response.PermissionResponse;
import com.m42hub.m42hub_api.user.dto.response.SystemRoleResponse;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.mapper.PermissionMapper;
import com.m42hub.m42hub_api.user.mapper.SystemRoleMapper;
import com.m42hub.m42hub_api.user.service.PermissionService;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/system-role")
@RequiredArgsConstructor
public class SystemRoleController {

    private final SystemRoleService systemRoleService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system_role:get_all')")
    public ResponseEntity<List<SystemRoleResponse>> getAll() {
        return ResponseEntity.ok(systemRoleService.findAll()
                .stream()
                .map(SystemRoleMapper::toSystemRoleResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system_role:get_by_id')")
    public ResponseEntity<SystemRoleResponse> getById(@PathVariable Long id) {
        return systemRoleService.findById(id)
                .map(systemRole -> ResponseEntity.ok(SystemRoleMapper.toSystemRoleResponse(systemRole)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system_role:create')")
    public ResponseEntity<SystemRoleResponse> save(@RequestBody SystemRoleRequest request) {
        SystemRole newSystemRole = SystemRoleMapper.toSystemRole(request);
        SystemRole savedSystemRole = systemRoleService.save(newSystemRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(SystemRoleMapper.toSystemRoleResponse(savedSystemRole));
    }


}
