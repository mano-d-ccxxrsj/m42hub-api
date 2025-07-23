package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
