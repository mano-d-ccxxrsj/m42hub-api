package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Tool;
import com.m42hub.m42hub_api.project.repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository repository;

    public List<Tool> findAll() {
        return repository.findAll();
    }

    public Optional<Tool> findById(Long id) {
        return repository.findById(id);
    }

    public Tool save(Tool tool) {
        return repository.save(tool);
    }

    public Optional<Tool> changeColor(Long toolId, String hexColor) {
        Optional<Tool> optTool = repository.findById(toolId);
        if (optTool.isPresent()) {
            Tool tool = optTool.get();
            tool.setHexColor(hexColor);

            repository.save(tool);
            return Optional.of(tool);
        }

        return Optional.empty();
    }

}
