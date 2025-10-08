package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.ProjectUnfilledRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectUnfilledRoleRepository extends JpaRepository<ProjectUnfilledRole, Long> {
    List<ProjectUnfilledRole> findByRoleIdIn(List<Long> roleIds);
    List<ProjectUnfilledRole> findByProjectId(UUID projectId);
    List<ProjectUnfilledRole> findByProjectIdIn(List<UUID> projectIds);
    void deleteByProjectIdAndRoleId(UUID projectId, Long roleId);
    void deleteByProjectId(UUID projectId);
}