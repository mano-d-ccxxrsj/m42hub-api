package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRolePermission;
import com.m42hub.m42hub_api.user.repository.PermissionRepository;
import com.m42hub.m42hub_api.user.repository.SystemRolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final SystemRolePermissionRepository systemRolePermissionRepository;
    private final PermissionRepository repository;

    @Transactional
    public Permission save(Permission permission) {
        return repository.save(permission);
    }

    @Transactional(readOnly = true)
    public Optional<Permission> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Permission> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void registerPermission(String name) {
        if (!repository.existsByName(name)) {
            Permission permission = Permission
                    .builder()
                    .name(name)
                    .build();
            repository.save(permission);
        }
    }

    @Transactional(readOnly = true)
    public List<Permission> findBySystemRoleId(UUID systemRoleId) {
        List<SystemRolePermission> rolePermissions = systemRolePermissionRepository.findBySystemRoleId(systemRoleId);
        List<UUID> permissionIds = rolePermissions.stream()
                .map(SystemRolePermission::getPermissionId)
                .collect(Collectors.toList());
        return repository.findAllById(permissionIds);
    }
}