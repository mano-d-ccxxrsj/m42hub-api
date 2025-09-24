package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.project.dto.request.ChangeUnfilledRolesRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectUpdateRequest;
import com.m42hub.m42hub_api.project.dto.response.PageResponse;
import com.m42hub.m42hub_api.project.dto.response.ProjectListItemResponse;
import com.m42hub.m42hub_api.project.dto.response.ProjectResponse;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.mapper.PageMapper;
import com.m42hub.m42hub_api.project.mapper.ProjectMapper;
import com.m42hub.m42hub_api.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping()
    public ResponseEntity<List<ProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.findAll()
                .stream()
                .map(ProjectMapper::toProjectResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        return projectService.findById(id)
                .map(project -> ResponseEntity.ok(ProjectMapper.toProjectResponse(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ProjectListItemResponse>> findByParams(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "10", required = false) Integer limit,
            @RequestParam(defaultValue = "id", required = false) String sortBy,
            @RequestParam(defaultValue = "ASC", required = false) String sortDirection,
            @RequestParam(required = false) List<Long> status,
            @RequestParam(required = false) List<Long> complexity,
            @RequestParam(required = false) List<Long> tools,
            @RequestParam(required = false) List<Long> topics,
            @RequestParam(required = false) List<Long> unfilledRoles
    ) {
        Page<Project> projectPage = projectService.findByParams(page, limit, sortBy, sortDirection, status, complexity, tools, topics, unfilledRoles);

        PageResponse<ProjectListItemResponse> response = PageMapper.toPagedResponse(projectPage, ProjectMapper::toProjectListResponse);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:create')")
    public ResponseEntity<ProjectResponse> save(@RequestBody @Valid ProjectRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        Project newProject = ProjectMapper.toProject(request, userData.id());
        Project savedProject = projectService.save(newProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectMapper.toProjectResponse(savedProject));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:update')")
    public ResponseEntity<ProjectResponse> update(@PathVariable long id, @RequestBody @Valid ProjectUpdateRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        Project updatedProject = ProjectMapper.toUpdatedProject(request);
        return projectService.update(id, updatedProject, userData.id()).map(project -> ResponseEntity.ok(ProjectMapper.toProjectResponse(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/unfilled-roles/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:change_unfilled_roles')")
    public ResponseEntity<ProjectResponse> changeUnfilledRoles(@PathVariable Long id, @RequestBody @Valid ChangeUnfilledRolesRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JWTUserData userData = (JWTUserData) authentication.getPrincipal();

        return projectService.changeUnfilledRoles(id, request.unfilledRoles(), userData.id())
                .map(project -> ResponseEntity.ok(ProjectMapper.toProjectResponse(project)))
                .orElse(ResponseEntity.notFound().build());
    }

}
