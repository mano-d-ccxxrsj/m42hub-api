package com.m42hub.m42hub_api.user.repository;

import com.m42hub.m42hub_api.user.entity.SystemRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SystemRolePermissionRepository extends JpaRepository<SystemRolePermission, Long> {
    List<SystemRolePermission> findBySystemRoleId(UUID roleId);
    List<SystemRolePermission> findByPermissionId(UUID projectId);
    List<SystemRolePermission> findBySystemRoleIdIn(List<UUID> systemRoleIds);
    void deleteBySystemRoleIdAndPermissionId(UUID projectId, UUID permissionId);
}