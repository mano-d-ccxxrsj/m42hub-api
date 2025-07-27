package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectRequest;
import com.m42hub.m42hub_api.project.dto.response.MemberResponse;
import com.m42hub.m42hub_api.project.dto.response.ProjectResponse;
import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.mapper.MemberMapper;
import com.m42hub.m42hub_api.project.mapper.ProjectMapper;
import com.m42hub.m42hub_api.project.service.MemberService;
import com.m42hub.m42hub_api.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:get_all')")
    public ResponseEntity<List<ProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.findAll()
                .stream()
                .map(ProjectMapper::toProjectResponse)
                .toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:get_by_id')")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(project -> ResponseEntity.ok(ProjectMapper.toProjectResponse(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:create')")
    public ResponseEntity<ProjectResponse> save(@RequestBody ProjectRequest request) {
        Project newProject = ProjectMapper.toProject(request);
        Project savedProject = projectService.save(newProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectMapper.toProjectResponse(savedProject));
    }

}
