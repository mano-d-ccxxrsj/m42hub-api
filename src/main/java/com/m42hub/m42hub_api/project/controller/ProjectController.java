package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.project.dto.request.ChangeUnfilledRolesRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectUpdateRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.project.mapper.*;
import com.m42hub.m42hub_api.project.service.*;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectUnfilledRoleService projectUnfilledRoleService;
    private final MemberStatusService memberStatusService;
    private final SystemRoleService systemRoleService;
    private final ComplexityService complexityService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final StatusService statusService;
    private final TopicService topicService;
    private final ToolService toolService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        List<ProjectResponse> responses = this.getAllProjectsWithBatchLoading();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable UUID id) {
        return projectService.findById(id)
                .map(project -> ResponseEntity.ok(this.getProjectResponseWithBatchLoading(project)))
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

        PageResponse<ProjectListItemResponse> response = this.getProjectListResponsesWithBatchLoading(projectPage);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:create')")
    public ResponseEntity<ProjectResponse> save(@RequestBody ProjectRequest request) {
        Project newProject = ProjectMapper.toProject(request);
        Project savedProject = projectService.save(newProject);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.getProjectResponseWithBatchLoading(savedProject));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:update')")
    public ResponseEntity<ProjectResponse> update(@PathVariable UUID id, @RequestBody ProjectUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        Project updatedProject = ProjectMapper.toUpdatedProject(request);
        return projectService.update(id, updatedProject, userData.id()).map(project -> ResponseEntity.ok(this.getProjectResponseWithBatchLoading(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/unfilled-roles/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('project:change_unfilled_roles')")
    public ResponseEntity<ProjectResponse> changeUnfilledRoles(@PathVariable UUID id, @RequestBody ChangeUnfilledRolesRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        return projectService.changeUnfilledRoles(id, request.unfilledRoles(), userData.id())
                .map(project -> ResponseEntity.ok(this.getProjectResponseWithBatchLoading(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/banner/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('user:change-project-banner')")
    public ResponseEntity<ProjectResponse> changeProjectBanner(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        return projectService.changeProjectBanner(file, id, userData.id())
                .map(project -> ResponseEntity.ok(this.getProjectResponseWithBatchLoading(project)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Métodos auxiliares, para não manchar a classe de Mapper com imports de serviço, e para não manchar a classe de Service com DTOs
    private List<ProjectResponse> getAllProjectsWithBatchLoading() {
        List<Project> projects = projectService.findAll();

        if (projects.isEmpty()) {
            return List.of();
        }

        List<UUID> projectIds = projects.stream().map(Project::getId).toList();
        List<Long> statusIds = projects.stream().map(Project::getStatusId).distinct().toList();
        List<Long> complexityIds = projects.stream().map(Project::getComplexityId).distinct().toList();

        Map<Long, Status> statusMap = statusService.findAllByIds(statusIds);
        Map<Long, Complexity> complexityMap = complexityService.findAllByIds(complexityIds);
        Map<UUID, List<Tool>> toolsByProject = toolService.findToolsByProjectIds(projectIds);
        Map<UUID, List<Topic>> topicsByProject = topicService.findTopicsByProjectIds(projectIds);
        Map<UUID, List<Role>> unfilledRolesByProject = projectUnfilledRoleService.findUnfilledRolesByProjectIds(projectIds);
        Map<UUID, List<Member>> membersByProject = memberService.findByProjectIds(projectIds);

        List<UUID> allUserIds = membersByProject.values().stream()
                .flatMap(List::stream)
                .map(Member::getUserId)
                .distinct()
                .toList();
        Map<UUID, User> userMap = userService.findAllByIds(allUserIds);

        List<UUID> allSystemRoleIds = userMap.values().stream()
                .map(User::getSystemRoleId)
                .distinct()
                .toList();
        Map<UUID, SystemRole> systemRoleMap = systemRoleService.findAllByIds(allSystemRoleIds);

        List<Long> allMemberStatusIds = membersByProject.values().stream()
                .flatMap(List::stream)
                .map(Member::getStatusId)
                .distinct()
                .toList();
        Map<Long, MemberStatus> memberStatusMap = memberStatusService.findAllByIds(allMemberStatusIds);

        return projects.stream()
                .map(project -> {
                    Status status = statusMap.get(project.getStatusId());
                    Complexity complexity = complexityMap.get(project.getComplexityId());
                    List<Tool> tools = toolsByProject.getOrDefault(project.getId(), List.of());
                    List<Topic> topics = topicsByProject.getOrDefault(project.getId(), List.of());
                    List<Role> unfilledRoles = unfilledRolesByProject.getOrDefault(project.getId(), List.of());
                    List<Member> members = membersByProject.getOrDefault(project.getId(), List.of());

                    StatusResponse statusResponse = Optional.ofNullable(status)
                            .map(StatusMapper::toStatusResponse)
                            .orElse(null);

                    ComplexityResponse complexityResponse = Optional.ofNullable(complexity)
                            .map(ComplexityMapper::toComplexityResponse)
                            .orElse(null);

                    List<ToolResponse> toolResponses = tools.stream()
                            .map(ToolMapper::toToolResponse)
                            .toList();

                    List<TopicResponse> topicResponses = topics.stream()
                            .map(TopicMapper::toTopicResponse)
                            .toList();

                    List<RoleResponse> unfilledRoleResponses = unfilledRoles.stream()
                            .map(RoleMapper::toRoleResponse)
                            .toList();

                    List<MemberResponse> memberResponses = members.stream()
                            .map(member -> {
                                User user = userMap.get(member.getUserId());
                                SystemRole systemRole = systemRoleMap.get(user.getSystemRoleId());
                                MemberStatus memberStatus = memberStatusMap.get(member.getStatusId());

                                AuthenticatedUserResponse authenticatedUserResponse = UserMapper.toAuthenticatedUserResponse(user, systemRole);
                                MemberStatusResponse memberStatusResponse = MemberStatusMapper.toMemberStatusResponse(memberStatus);
                                return MemberMapper.toMemberResponse(authenticatedUserResponse, memberStatusResponse, member);
                            })
                            .toList();

                    return ProjectMapper.toProjectResponse(
                            project,
                            statusResponse,
                            complexityResponse,
                            toolResponses,
                            topicResponses,
                            unfilledRoleResponses,
                            memberResponses
                    );
                })
                .toList();
    }

    private PageResponse<ProjectListItemResponse> getProjectListResponsesWithBatchLoading(Page<Project> projectPage) {
        List<Project> projects = projectPage.getContent();

        if (projects.isEmpty()) {
            PaginationResponse pagination = new PaginationResponse(
                    projectPage.getNumber(),
                    projectPage.getTotalPages(),
                    projectPage.getTotalElements()
            );
            return new PageResponse<>(List.of(), pagination);
        }

        List<UUID> projectIds = projects.stream().map(Project::getId).toList();
        List<Long> statusIds = projects.stream().map(Project::getStatusId).distinct().toList();
        List<Long> complexityIds = projects.stream().map(Project::getComplexityId).distinct().toList();

        Map<Long, Status> statusMap = statusService.findAllByIds(statusIds);
        Map<Long, Complexity> complexityMap = complexityService.findAllByIds(complexityIds);
        Map<UUID, List<Tool>> toolsByProject = toolService.findToolsByProjectIds(projectIds);
        Map<UUID, List<Topic>> topicsByProject = topicService.findTopicsByProjectIds(projectIds);
        Map<UUID, List<Role>> unfilledRolesByProject = projectUnfilledRoleService.findUnfilledRolesByProjectIds(projectIds);

        Map<UUID, List<Member>> membersByProject = memberService.findByProjectIds(projectIds);
        List<UUID> managerUserIds = membersByProject.values().stream()
                .flatMap(List::stream)
                .filter(Member::getIsManager)
                .map(Member::getUserId)
                .distinct()
                .toList();
        Map<UUID, User> managerUserMap = userService.findAllByIds(managerUserIds);

        List<ProjectListItemResponse> content = projects.stream()
                .map(project -> {
                    Status status = statusMap.get(project.getStatusId());
                    Complexity complexity = complexityMap.get(project.getComplexityId());
                    List<Tool> tools = toolsByProject.getOrDefault(project.getId(), List.of());
                    List<Topic> topics = topicsByProject.getOrDefault(project.getId(), List.of());
                    List<Role> unfilledRoles = unfilledRolesByProject.getOrDefault(project.getId(), List.of());
                    List<Member> members = membersByProject.getOrDefault(project.getId(), List.of());

                    String statusName = status != null ? status.getName() : null;
                    String complexityName = complexity != null ? complexity.getName() : null;
                    List<String> toolNames = tools.stream().map(Tool::getName).toList();
                    List<String> topicNames = topics.stream().map(Topic::getName).toList();
                    List<String> unfilledRoleNames = unfilledRoles.stream().map(Role::getName).toList();

                    String manager = members.stream()
                            .filter(Member::getIsManager)
                            .findFirst()
                            .map(member -> {
                                User user = managerUserMap.get(member.getUserId());
                                return user != null ? user.getFirstName() + " " + user.getLastName() : null;
                            })
                            .orElse(null);

                    return ProjectMapper.toProjectListResponse(
                            project,
                            statusName,
                            complexityName,
                            toolNames,
                            topicNames,
                            unfilledRoleNames,
                            manager
                    );
                })
                .toList();

        PaginationResponse pagination = new PaginationResponse(
                projectPage.getNumber(),
                projectPage.getTotalPages(),
                projectPage.getTotalElements()
        );

        return new PageResponse<>(content, pagination);
    }

    private ProjectResponse getProjectResponseWithBatchLoading(Project project) {
        List<Project> singleProjectList = List.of(project);
        List<UUID> projectIds = singleProjectList.stream().map(Project::getId).toList();
        List<Long> statusIds = singleProjectList.stream().map(Project::getStatusId).distinct().toList();
        List<Long> complexityIds = singleProjectList.stream().map(Project::getComplexityId).distinct().toList();

        Map<Long, Status> statusMap = statusService.findAllByIds(statusIds);
        Map<Long, Complexity> complexityMap = complexityService.findAllByIds(complexityIds);
        Map<UUID, List<Tool>> toolsByProject = toolService.findToolsByProjectIds(projectIds);
        Map<UUID, List<Topic>> topicsByProject = topicService.findTopicsByProjectIds(projectIds);
        Map<UUID, List<Role>> unfilledRolesByProject = projectUnfilledRoleService.findUnfilledRolesByProjectIds(projectIds);
        Map<UUID, List<Member>> membersByProject = memberService.findByProjectIds(projectIds);

        List<UUID> allUserIds = membersByProject.values().stream()
                .flatMap(List::stream)
                .map(Member::getUserId)
                .distinct()
                .toList();
        Map<UUID, User> userMap = userService.findAllByIds(allUserIds);

        List<UUID> allSystemRoleIds = userMap.values().stream()
                .map(User::getSystemRoleId)
                .distinct()
                .toList();
        Map<UUID, SystemRole> systemRoleMap = systemRoleService.findAllByIds(allSystemRoleIds);

        List<Long> allMemberStatusIds = membersByProject.values().stream()
                .flatMap(List::stream)
                .map(Member::getStatusId)
                .distinct()
                .toList();
        Map<Long, MemberStatus> memberStatusMap = memberStatusService.findAllByIds(allMemberStatusIds);

        Status status = statusMap.get(project.getStatusId());
        Complexity complexity = complexityMap.get(project.getComplexityId());
        List<Tool> tools = toolsByProject.getOrDefault(project.getId(), List.of());
        List<Topic> topics = topicsByProject.getOrDefault(project.getId(), List.of());
        List<Role> unfilledRoles = unfilledRolesByProject.getOrDefault(project.getId(), List.of());
        List<Member> members = membersByProject.getOrDefault(project.getId(), List.of());

        StatusResponse statusResponse = Optional.ofNullable(status)
                .map(StatusMapper::toStatusResponse)
                .orElse(null);

        ComplexityResponse complexityResponse = Optional.ofNullable(complexity)
                .map(ComplexityMapper::toComplexityResponse)
                .orElse(null);

        List<ToolResponse> toolResponses = tools.stream()
                .map(ToolMapper::toToolResponse)
                .toList();

        List<TopicResponse> topicResponses = topics.stream()
                .map(TopicMapper::toTopicResponse)
                .toList();

        List<RoleResponse> unfilledRoleResponses = unfilledRoles.stream()
                .map(RoleMapper::toRoleResponse)
                .toList();

        List<MemberResponse> memberResponses = members.stream()
                .map(member -> {
                    User user = userMap.get(member.getUserId());
                    SystemRole systemRole = systemRoleMap.get(user.getSystemRoleId());
                    MemberStatus memberStatus = memberStatusMap.get(member.getStatusId());

                    AuthenticatedUserResponse authenticatedUserResponse = UserMapper.toAuthenticatedUserResponse(user, systemRole);
                    MemberStatusResponse memberStatusResponse = MemberStatusMapper.toMemberStatusResponse(memberStatus);
                    return MemberMapper.toMemberResponse(authenticatedUserResponse, memberStatusResponse, member);
                })
                .toList();

        return ProjectMapper.toProjectResponse(
                project,
                statusResponse,
                complexityResponse,
                toolResponses,
                topicResponses,
                unfilledRoleResponses,
                memberResponses
        );
    }
}