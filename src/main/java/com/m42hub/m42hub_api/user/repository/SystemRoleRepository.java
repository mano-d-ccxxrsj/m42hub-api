package com.m42hub.m42hub_api.user.repository;

import com.m42hub.m42hub_api.user.entity.SystemRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SystemRoleRepository extends JpaRepository<SystemRole, UUID> {
}