package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.ProjectTopic;
import com.m42hub.m42hub_api.project.repository.ProjectTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectTopicService {

    private final ProjectTopicRepository projectTopicRepository;

    @Transactional(readOnly = true)
    public Optional<ProjectTopic> findById(Long id) {
        return projectTopicRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectTopic> findAll() {
        return projectTopicRepository.findAll();
    }

    public List<ProjectTopic> findByProjectId(UUID projectId) {
        return projectTopicRepository.findByProjectId(projectId);
    }

    public Set<UUID> getProjectIdsByTopicIds(List<Long> topicIds) {
        if (topicIds == null || topicIds.isEmpty()) return Collections.emptySet();
        return projectTopicRepository.findByTopicIdIn(topicIds).stream()
                .map(ProjectTopic::getProjectId)
                .collect(Collectors.toSet());
    }
}