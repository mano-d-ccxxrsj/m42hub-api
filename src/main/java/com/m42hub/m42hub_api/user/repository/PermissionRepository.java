package com.m42hub.m42hub_api.user.repository;

import com.m42hub.m42hub_api.user.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    boolean existsByName(String name);
}