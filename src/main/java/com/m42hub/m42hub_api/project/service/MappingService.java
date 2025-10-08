package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MappingService {
    private final StatusRepository statusRepository;
    private final ComplexityRepository complexityRepository;
    private final ProjectToolRepository projectToolRepository;
    private final ProjectTopicRepository projectTopicRepository;
    private final RoleRepository roleRepository;

    private final Map<Integer, Long> statusMapping = new HashMap<>();
    private final Map<Integer, Long> complexityMapping = new HashMap<>();
    private final Map<Integer, Long> toolMapping = new HashMap<>();
    private final Map<Integer, Long> topicMapping = new HashMap<>();
    private final Map<Integer, Long> roleMapping = new HashMap<>();

    private int statusCounter = 1;
    private int complexityCounter = 1;
    private int toolCounter = 1;
    private int topicCounter = 1;
    private int roleCounter = 1;

    @PostConstruct
    public void init() {
        statusRepository.findAll().forEach(status -> {
            statusMapping.put(statusCounter++, status.getId());
        });

        complexityRepository.findAll().forEach(complexity -> {
            complexityMapping.put(complexityCounter++, complexity.getId());
        });

        projectToolRepository.findAll().forEach(tool -> {
            toolMapping.put(toolCounter++, tool.getId());
        });

        projectTopicRepository.findAll().forEach(topic -> {
            topicMapping.put(topicCounter++, topic.getId());
        });

        roleRepository.findAll().forEach(role -> {
            roleMapping.put(roleCounter++, role.getId());
        });
    }

    public Long getStatusUuid(Integer intId) {
        return statusMapping.get(intId);
    }

    public Long getComplexityUuid(Integer intId) {
        return complexityMapping.get(intId);
    }

    public Long getToolUuid(Integer intId) {
        return toolMapping.get(intId);
    }

    public Long getTopicUuid(Integer intId) {
        return topicMapping.get(intId);
    }

    public Long getRoleUuid(Integer intId) {
        return roleMapping.get(intId);
    }
}