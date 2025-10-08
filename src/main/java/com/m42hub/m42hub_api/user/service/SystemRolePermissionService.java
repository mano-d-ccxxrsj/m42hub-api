package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.SystemRolePermission;
import com.m42hub.m42hub_api.user.repository.PermissionRepository;
import com.m42hub.m42hub_api.user.repository.SystemRolePermissionRepository;
import com.m42hub.m42hub_api.user.repository.SystemRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemRolePermissionService {

    private final SystemRolePermissionRepository systemRolePermissionRepository;
    private final SystemRoleRepository systemRoleRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public void addRelation(UUID systemRoleId, UUID permissionId) {
        if (!systemRoleRepository.existsById(systemRoleId)) {
            throw new IllegalArgumentException("SystemRole não existe");
        }
        if (!permissionRepository.existsById(permissionId)) {
            throw new IllegalArgumentException("Permission não existe");
        }

        SystemRolePermission relation = SystemRolePermission.builder()
                .systemRoleId(systemRoleId)
                .permissionId(permissionId)
                .build();

        systemRolePermissionRepository.save(relation);
    }

    @Transactional
    public void addRelations(UUID systemRoleId, List<UUID> permissionIds) {
        if (!systemRoleRepository.existsById(systemRoleId)) {
            throw new IllegalArgumentException("SystemRole não existe");
        }

        List<Permission> validPermissions = permissionRepository.findAllById(permissionIds);
        List<UUID> validPermissionIds = validPermissions.stream()
                .map(Permission::getId)
                .toList();

        List<SystemRolePermission> relations = validPermissionIds.stream()
                .map(permissionId -> SystemRolePermission.builder()
                        .systemRoleId(systemRoleId)
                        .permissionId(permissionId)
                        .build())
                .toList();

        if (!relations.isEmpty()) {
            systemRolePermissionRepository.saveAll(relations);
        }
    }

    @Transactional(readOnly = true)
    public Optional<SystemRolePermission> findById(Long id) {
        return systemRolePermissionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<SystemRolePermission> findAll() {
        return systemRolePermissionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Permission> findPermissionsBySystemRole(UUID systemRoleId) {
        List<SystemRolePermission> relations = systemRolePermissionRepository.findBySystemRoleId(systemRoleId);
        List<UUID> permissionIds = relations.stream()
                .map(SystemRolePermission::getPermissionId)
                .toList();
        return permissionRepository.findAllById(permissionIds);
    }

    @Transactional(readOnly = true)
    public Map<UUID, List<Permission>> findPermissionsBySystemRoleIds(List<UUID> systemRoleIds) {
        List<SystemRolePermission> allRelations = systemRolePermissionRepository.findBySystemRoleIdIn(systemRoleIds);

        List<UUID> allPermissionIds = allRelations.stream()
                .map(SystemRolePermission::getPermissionId)
                .distinct()
                .toList();

        List<Permission> allPermissions = permissionRepository.findAllById(allPermissionIds);

        Map<UUID, Permission> permissionMap = allPermissions.stream()
                .collect(Collectors.toMap(Permission::getId, p -> p));

        return allRelations.stream()
                .collect(Collectors.groupingBy(
                        SystemRolePermission::getSystemRoleId,
                        Collectors.mapping(
                                relation -> permissionMap.get(relation.getPermissionId()),
                                Collectors.toList()
                        )
                ));
    }

    @Transactional
    public void removeRelation(UUID systemRoleId, UUID permissionId) {
        systemRolePermissionRepository.deleteBySystemRoleIdAndPermissionId(systemRoleId, permissionId);
    }

    @Transactional
    public Optional<SystemRole> updatePermissions(UUID systemRoleId, List<UUID> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return systemRoleRepository.findById(systemRoleId);
        }

        List<Permission> validPermissions = permissionRepository.findAllById(permissionIds);
        Set<UUID> validPermissionIds = validPermissions.stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        List<SystemRolePermission> existingRelations = systemRolePermissionRepository.findBySystemRoleId(systemRoleId);
        Set<UUID> existingPermissionIds = existingRelations.stream()
                .map(SystemRolePermission::getPermissionId)
                .collect(Collectors.toSet());

        List<SystemRolePermission> newRelations = validPermissionIds.stream()
                .filter(id -> !existingPermissionIds.contains(id))
                .map(permissionId -> SystemRolePermission.builder()
                        .systemRoleId(systemRoleId)
                        .permissionId(permissionId)
                        .build())
                .toList();

        if (!newRelations.isEmpty()) {
            systemRolePermissionRepository.saveAll(newRelations);
        }

        return systemRoleRepository.findById(systemRoleId);
    }
}