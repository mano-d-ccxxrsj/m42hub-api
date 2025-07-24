package com.m42hub.m42hub_api.user.controller;

import com.m42hub.m42hub_api.user.dto.request.PermissionRequest;
import com.m42hub.m42hub_api.user.dto.response.PermissionResponse;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.mapper.PermissionMapper;
import com.m42hub.m42hub_api.user.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping()
    public ResponseEntity<List<PermissionResponse>> getAll() {
        return ResponseEntity.ok(permissionService.findAll()
                .stream()
                .map(PermissionMapper::toPermissionResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(permission -> ResponseEntity.ok(PermissionMapper.toPermissionResponse(permission)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PermissionResponse> save(@RequestBody PermissionRequest request) {
        Permission newPermission = PermissionMapper.toPermission(request);
        Permission savedPermission = permissionService.save(newPermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(PermissionMapper.toPermissionResponse(savedPermission));
    }


}
