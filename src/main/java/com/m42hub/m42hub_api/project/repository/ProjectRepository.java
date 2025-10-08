package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findByStatusIdIn(List<Long> statusIds);
    List<Project> findByComplexityIdIn(List<Long> complexityIds);
    Optional<Project> findByName(String name);
}