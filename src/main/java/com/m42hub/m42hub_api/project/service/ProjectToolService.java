package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.ProjectTool;
import com.m42hub.m42hub_api.project.repository.ProjectToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectToolService {

    private final ProjectToolRepository projectToolRepository;

    @Transactional(readOnly = true)
    public Optional<ProjectTool> findById(Long id) {
        return projectToolRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectTool> findAll() {
        return projectToolRepository.findAll();
    }

    public List<ProjectTool> findByProjectId(UUID projectId) {
        return projectToolRepository.findByProjectId(projectId);
    }

    // Métodos auxiliares:
    // Caller em ProjectService usa Transactional, então não se deve anotar este.
    public Set<UUID> getProjectIdsByToolIds(List<Long> toolIds) {
        if (toolIds == null || toolIds.isEmpty()) return Collections.emptySet();
        return projectToolRepository.findByToolIdIn(toolIds).stream()
                .map(ProjectTool::getProjectId)
                .collect(Collectors.toSet());
    }
}