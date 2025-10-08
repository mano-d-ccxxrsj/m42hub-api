package com.m42hub.m42hub_api.project.controller;

import com.m42hub.m42hub_api.config.JWTUserData;
import com.m42hub.m42hub_api.exceptions.CustomNotFoundException;
import com.m42hub.m42hub_api.project.dto.request.MemberRejectRequest;
import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.project.mapper.MemberMapper;
import com.m42hub.m42hub_api.project.mapper.MemberStatusMapper;
import com.m42hub.m42hub_api.project.mapper.RoleMapper;
import com.m42hub.m42hub_api.project.service.*;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/project/member")
@RequiredArgsConstructor
public class MemberController {

    private final ProjectUnfilledRoleService projectUnfilledRoleService;
    private final MemberStatusService memberStatusService;
    private final ComplexityService complexityService;
    private final SystemRoleService systemRoleService;
    private final ProjectService projectService;
    private final MemberService memberService;
    private final StatusService statusService;
    private final TopicService topicService;
    private final UserService userService;
    private final ToolService toolService;
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:get_all')")
    public ResponseEntity<List<MemberResponse>> getAll() {
        List<Member> members = memberService.findAll();
        List<MemberResponse> responses = getMemberResponsesWithBatchLoading(members);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:get_by_id')")
    public ResponseEntity<MemberResponse> getById(@PathVariable Long id) {
        return memberService.findById(id)
                .map(member -> {
                    List<MemberResponse> responses = getMemberResponsesWithBatchLoading(List.of(member));
                    return ResponseEntity.ok(responses.getFirst());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:get_by_username')")
    public ResponseEntity<List<MemberProjectResponse>> getByUsername(@PathVariable String username) {
        List<Member> members = memberService.findByUsername(username);
        List<MemberProjectResponse> responses = getMemberProjectResponsesWithBatchLoading(members);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:create')")
    public ResponseEntity<MemberResponse> save(@RequestBody MemberRequest request) {
        MemberStatus foundMemberStatus = memberStatusService.findByName("Aprovado").orElseThrow(() ->
                new CustomNotFoundException("MemberStatus não encontrado")
        );
        Member newMember = MemberMapper.toMember(request, foundMemberStatus);
        Member savedMember = memberService.save(newMember);
        List<MemberResponse> responses = getMemberResponsesWithBatchLoading(List.of(savedMember));
        return ResponseEntity.status(HttpStatus.CREATED).body(responses.getFirst());
    }

    @PostMapping("/apply")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:create')")
    public ResponseEntity<MemberResponse> apply(@RequestBody MemberRequest request) {
        MemberStatus foundMemberStatus = memberStatusService.findByName("Aprovado").orElseThrow(() ->
                new CustomNotFoundException("MemberStatus não encontrado")
        );
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        Member newMember = MemberMapper.toMemberApply(request, userData.id(), foundMemberStatus);
        Member savedMember = memberService.save(newMember);
        List<MemberResponse> responses = getMemberResponsesWithBatchLoading(List.of(savedMember));
        return ResponseEntity.status(HttpStatus.CREATED).body(responses.getFirst());
    }

    @PatchMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:approve')")
    public ResponseEntity<MemberResponse> approve(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        return memberService.approve(id, userData.id())
                .map(member -> {
                    List<MemberResponse> responses = getMemberResponsesWithBatchLoading(List.of(member));
                    return ResponseEntity.ok(responses.getFirst());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('member:reject')")
    public ResponseEntity<MemberResponse> reject(@PathVariable Long id, @RequestBody MemberRejectRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTUserData userData = (JWTUserData) authentication.getPrincipal();
        return memberService.reject(id, request.applicationFeedback(), userData.id())
                .map(member -> {
                    List<MemberResponse> responses = getMemberResponsesWithBatchLoading(List.of(member));
                    return ResponseEntity.ok(responses.getFirst());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Métodos auxiliares, para não manchar a classe de Mapper com imports de serviço, e para não manchar a classe de Service com DTOs
    // Seguindo modelo do mapper no github:
    // https://github.com/m42hub/m42hub-api/blob/main/src/main/java/com/m42hub/m42hub_api/project/mapper/MemberMapper.java#L71
    private List<MemberResponse> getMemberResponsesWithBatchLoading(List<Member> members) {
        if (members.isEmpty()) {
            return List.of();
        }

        List<UUID> userIds = members.stream().map(Member::getUserId).distinct().toList();
        List<Long> memberStatusIds = members.stream().map(Member::getStatusId).distinct().toList();

        Map<UUID, User> userMap = userService.findAllByIds(userIds);
        Map<Long, MemberStatus> memberStatusMap = memberStatusService.findAllByIds(memberStatusIds);

        List<UUID> systemRoleIds = userMap.values().stream()
                .map(User::getSystemRoleId)
                .distinct()
                .toList();
        Map<UUID, SystemRole> systemRoleMap = systemRoleService.findAllByIds(systemRoleIds);

        return members.stream()
                .map(member -> {
                    User user = userMap.get(member.getUserId());
                    SystemRole systemRole = systemRoleMap.get(user.getSystemRoleId());
                    MemberStatus memberStatus = memberStatusMap.get(member.getStatusId());

                    AuthenticatedUserResponse authenticatedUserResponse = UserMapper.toAuthenticatedUserResponse(user, systemRole);
                    MemberStatusResponse memberStatusResponse = MemberStatusMapper.toMemberStatusResponse(memberStatus);
                    return MemberMapper.toMemberResponse(authenticatedUserResponse, memberStatusResponse, member);
                })
                .toList();
    }

    private List<MemberProjectResponse> getMemberProjectResponsesWithBatchLoading(List<Member> members) {
        if (members.isEmpty()) {
            return List.of();
        }

        List<UUID> userIds = members.stream().map(Member::getUserId).distinct().toList();
        List<Long> memberStatusIds = members.stream().map(Member::getStatusId).distinct().toList();
        List<UUID> projectIds = members.stream().map(Member::getProjectId).distinct().toList();
        List<Long> roleIds = members.stream().map(Member::getRoleId).distinct().toList();

        Map<UUID, User> userMap = userService.findAllByIds(userIds);
        Map<Long, MemberStatus> memberStatusMap = memberStatusService.findAllByIds(memberStatusIds);
        Map<UUID, Project> projectMap = projectService.findAllByIds(projectIds);
        Map<Long, Role> roleMap = roleService.findAllByIds(roleIds);

        List<UUID> systemRoleIds = userMap.values().stream()
                .map(User::getSystemRoleId)
                .distinct()
                .toList();
        Map<UUID, SystemRole> systemRoleMap = systemRoleService.findAllByIds(systemRoleIds);

        Map<UUID, List<Tool>> toolsByProject = toolService.findToolsByProjectIds(projectIds);
        Map<UUID, List<Topic>> topicsByProject = topicService.findTopicsByProjectIds(projectIds);
        Map<UUID, List<Role>> unfilledRolesByProject = projectUnfilledRoleService.findUnfilledRolesByProjectIds(projectIds);

        Map<Long, Status> statusMap = statusService.findAllByIds(
                projectMap.values().stream().map(Project::getStatusId).distinct().toList()
        );

        Map<Long, Complexity> complexityMap = complexityService.findAllByIds(
                projectMap.values().stream().map(Project::getComplexityId).distinct().toList()
        );

        Map<UUID, String> projectManagers = new HashMap<>();
        Map<UUID, List<Member>> membersByProject = memberService.findByProjectIds(projectIds);

        for (UUID projectId : projectIds) {
            List<Member> projectMembers = membersByProject.get(projectId);
            if (projectMembers != null) {
                Optional<Member> manager = projectMembers.stream()
                        .filter(member -> Boolean.TRUE.equals(member.getIsManager()))
                        .findFirst();

                if (manager.isPresent()) {
                    User managerUser = userMap.get(manager.get().getUserId());
                    if (managerUser != null) {
                        projectManagers.put(projectId, managerUser.getFirstName() + " " + managerUser.getLastName());
                    }
                }
            }
        }

        return members.stream()
                .map(member -> {
                    User user = userMap.get(member.getUserId());
                    SystemRole systemRole = systemRoleMap.get(user.getSystemRoleId());
                    MemberStatus memberStatus = memberStatusMap.get(member.getStatusId());
                    Project project = projectMap.get(member.getProjectId());
                    Role role = roleMap.get(member.getRoleId());

                    AuthenticatedUserResponse authenticatedUserResponse = UserMapper.toAuthenticatedUserResponse(user, systemRole);
                    MemberStatusResponse memberStatusResponse = MemberStatusMapper.toMemberStatusResponse(memberStatus);
                    RoleResponse roleResponse = RoleMapper.toRoleResponse(role);

                    List<String> toolNames = toolsByProject.getOrDefault(member.getProjectId(), List.of())
                            .stream().map(Tool::getName).toList();

                    List<String> topicNames = topicsByProject.getOrDefault(member.getProjectId(), List.of())
                            .stream().map(Topic::getName).toList();

                    List<String> unfilledRoleNames = unfilledRolesByProject.getOrDefault(member.getProjectId(), List.of())
                            .stream().map(Role::getName).toList();

                    String managerName = projectManagers.get(member.getProjectId());
                    String statusName = statusMap.get(project.getStatusId()).getName();
                    String complexityName = complexityMap.get(project.getComplexityId()).getName();

                    ProjectListItemResponse projectListItemResponse = new ProjectListItemResponse(
                            project.getId(),
                            project.getName(),
                            project.getSummary(),
                            statusName,
                            complexityName,
                            project.getImageUrl(),
                            project.getCreatedAt(),
                            project.getStartDate(),
                            project.getEndDate(),
                            toolNames,
                            topicNames,
                            unfilledRoleNames,
                            managerName,
                            project.getDiscord(),
                            project.getGithub(),
                            project.getProjectWebsite()
                    );

                    return MemberMapper.toMemberProjectsResponse(
                            authenticatedUserResponse,
                            projectListItemResponse,
                            memberStatusResponse,
                            roleResponse,
                            member
                    );
                })
                .toList();
    }
}