package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.ChangeColorRequest;
import com.m42hub.m42hub_api.project.dto.request.ToolRequest;
import com.m42hub.m42hub_api.project.dto.response.ToolResponse;
import com.m42hub.m42hub_api.project.entity.Tool;
import com.m42hub.m42hub_api.project.mapper.ToolMapper;
import com.m42hub.m42hub_api.project.service.ToolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project/tool")
@RequiredArgsConstructor
public class ToolController {

    private final ToolService toolService;

    @GetMapping()
    public ResponseEntity<List<ToolResponse>> getAll() {
        return ResponseEntity.ok(toolService.findAll()
                .stream()
                .map(ToolMapper::toToolResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolResponse> getById(@PathVariable Long id) {
        return toolService.findById(id)
                .map(tool -> ResponseEntity.ok(ToolMapper.toToolResponse(tool)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('tool:create')")
    public ResponseEntity<ToolResponse> save(@RequestBody @Valid ToolRequest request) {
        Tool newTool = ToolMapper.toTool(request);
        Tool savedTool = toolService.save(newTool);
        return ResponseEntity.status(HttpStatus.CREATED).body(ToolMapper.toToolResponse(savedTool));
    }

    @PatchMapping("/color/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('tool:change_color')")
    public ResponseEntity<ToolResponse> changeColor(@PathVariable Long id, @RequestBody @Valid ChangeColorRequest request) {
        return toolService.changeColor(id, request.hexColor())
                .map(tool -> ResponseEntity.ok(ToolMapper.toToolResponse(tool)))
                .orElse(ResponseEntity.notFound().build());
    }

}
