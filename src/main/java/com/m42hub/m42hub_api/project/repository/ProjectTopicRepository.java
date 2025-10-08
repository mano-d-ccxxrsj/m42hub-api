package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.ProjectTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectTopicRepository extends JpaRepository<ProjectTopic, Long> {
    List<ProjectTopic> findByTopicIdIn(List<Long> topicIds);
    List<ProjectTopic> findByProjectId(UUID projectId);
    List<ProjectTopic> findByProjectIdIn(List<UUID> projectIds);
}