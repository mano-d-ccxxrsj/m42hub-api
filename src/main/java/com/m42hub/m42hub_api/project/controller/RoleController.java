package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.RoleRequest;
import com.m42hub.m42hub_api.project.dto.response.RoleResponse;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.mapper.RoleMapper;
import com.m42hub.m42hub_api.project.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping()
    public ResponseEntity<List<RoleResponse>> getAll() {
        return ResponseEntity.ok(roleService.findAll()
                .stream()
                .map(RoleMapper::toRoleResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(role -> ResponseEntity.ok(RoleMapper.toRoleResponse(role)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RoleResponse> save(@RequestBody RoleRequest request) {
        Role newRole = RoleMapper.toRole(request);
        Role savedRole = roleService.save(newRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleMapper.toRoleResponse(savedRole));
    }


}
