package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.ProjectTool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectToolRepository extends JpaRepository<ProjectTool, Long> {
    List<ProjectTool> findByToolIdIn(List<Long> toolIds);
    List<ProjectTool> findByProjectId(UUID projectId);
    List<ProjectTool> findByProjectIdIn(List<UUID> projectIds);
}