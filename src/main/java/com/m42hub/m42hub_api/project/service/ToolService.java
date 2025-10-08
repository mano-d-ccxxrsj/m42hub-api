package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.ProjectTool;
import com.m42hub.m42hub_api.project.entity.Tool;
import com.m42hub.m42hub_api.project.repository.ProjectToolRepository;
import com.m42hub.m42hub_api.project.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ProjectToolRepository projectToolRepository;
    private final ToolRepository repository;

    @Transactional
    public Tool save(Tool tool) {
        return repository.save(tool);
    }

    @Transactional(readOnly = true)
    public Optional<Tool> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Tool> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Optional<Tool> changeColor(Long toolId, String hexColor) {
        return repository.findById(toolId).map(tool -> {
            Optional.ofNullable(hexColor).ifPresent(tool::setHexColor);
            return repository.save(tool);
        });
    }

    @Transactional(readOnly = true)
    public List<Tool> findToolsByProjectId(UUID projectId) {
        List<ProjectTool> projectTools = projectToolRepository.findByProjectId(projectId);
        List<Long> toolIds = projectTools.stream()
                .map(ProjectTool::getToolId)
                .toList();
        return repository.findAllById(toolIds);
    }

    @Transactional(readOnly = true)
    public Map<UUID, List<Tool>> findToolsByProjectIds(List<UUID> projectIds) {
        List<ProjectTool> projectTools = projectToolRepository.findByProjectIdIn(projectIds);
        List<Long> toolIds = projectTools.stream().map(ProjectTool::getToolId).distinct().toList();
        List<Tool> tools = repository.findAllById(toolIds);
        Map<Long, Tool> toolMap = tools.stream().collect(Collectors.toMap(Tool::getId, Function.identity()));

        return projectTools.stream()
                .collect(Collectors.groupingBy(ProjectTool::getProjectId,
                        Collectors.mapping(pt -> toolMap.get(pt.getToolId()), Collectors.toList())));
    }
}