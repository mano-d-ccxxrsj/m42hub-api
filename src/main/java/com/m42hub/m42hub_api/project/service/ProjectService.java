package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.exceptions.ConflictException;
import com.m42hub.m42hub_api.exceptions.UnauthorizedException;
import com.m42hub.m42hub_api.exceptions.UsernameOrPasswordInvalidException;
import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.project.repository.ProjectRepository;
import com.m42hub.m42hub_api.project.specification.ProjectSpecification;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository repository;
    private final StatusService statusService;
    private final ComplexityService complexityService;
    private final ToolService toolService;
    private final TopicService topicService;
    private final RoleService roleService;
    private final MemberStatusService memberStatusService;
    private final UserService userService;


    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Project> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Project> findByParams(
            Integer page,
            Integer limit,
            String sortBy,
            String sortDirection,
            List<Long> status,
            List<Long> complexity,
            List<Long> tools,
            List<Long> topics,
            List<Long> unfilledRoles
    ) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Project> spec = Specification.allOf();

        if (status != null) {
            spec = spec.and(ProjectSpecification.status(status));
        }

        if (complexity != null) {
            spec = spec.and(ProjectSpecification.complexity(complexity));
        }

        if (tools != null) {
            spec = spec.and(ProjectSpecification.tools(tools));
        }

        if (topics != null) {
            spec = spec.and(ProjectSpecification.topics(topics));
        }

        if (unfilledRoles != null) {
            spec = spec.and(ProjectSpecification.unfilledRoles(unfilledRoles));
        }

        return repository.findAll(spec, pageable);
    }

    @Transactional
    public Project save(Project project) {
        project.setStatus(this.findStatus(project.getStatus()));
        project.setComplexity(this.findComplexity(project.getComplexity()));
        project.setTools(this.findTools(project.getTools()));
        project.setTopics(this.findTopics(project.getTopics()));
        project.setUnfilledRoles(this.findUnfilledRoles(project.getUnfilledRoles()));
        project.getMembers().forEach(member -> {
            member.setMemberStatus(this.findMemberStatus(member.getMemberStatus()));
            member.setUser(this.findUser(member.getUser()));
        });

        return repository.save(project);
    }

    public Optional<Project> update(Long projectId, Project updatedProject, Long userId) {
        Optional<Project> optProject = repository.findById(projectId);

        if(optProject.isPresent()) {
            Project project = optProject.get();

            if (this.isNotManager(project, userId)) throw new UnauthorizedException("Usuário que solicitou alteração não é o idealizador do projeto");

            if (updatedProject.getName() != null) project.setName(updatedProject.getName());
            if (updatedProject.getSummary() != null) project.setSummary(updatedProject.getSummary());
            if (updatedProject.getDescription() != null) project.setDescription(updatedProject.getDescription());
            if (updatedProject.getImageUrl() != null) project.setImageUrl(updatedProject.getImageUrl());
            if (updatedProject.getStartDate() != null) project.setStartDate(updatedProject.getStartDate());
            if (updatedProject.getEndDate() != null) project.setEndDate(updatedProject.getEndDate());

            if (updatedProject.getStatus() != null) {
                project.setStatus(findStatus(updatedProject.getStatus()));
            }

            if (updatedProject.getComplexity() != null) {
                project.setComplexity(findComplexity(updatedProject.getComplexity()));
            }

            if (updatedProject.getTools() != null) {
                project.setTools(findTools(updatedProject.getTools()));
            }

            if (updatedProject.getTopics() != null) {
                project.setTopics(findTopics(updatedProject.getTopics()));
            }

            if (updatedProject.getUnfilledRoles() != null) {

                List<Role> unfilledRolesFound = findUnfilledRoles(updatedProject.getUnfilledRoles());

                boolean hasFilledRole = unfilledRolesFound.stream()
                        .anyMatch(role -> isRoleFilled(project, role));

                if (hasFilledRole) throw new ConflictException("Existem cargos que já estão preenchidos");

                project.setUnfilledRoles(unfilledRolesFound);
            }

            repository.save(project);
            return Optional.of(project);

        }
        return Optional.empty();
    }

    public Optional<Project> changeUnfilledRoles(Long projectId, List<Long> unfilledRoleIds, Long userId) {
        Optional<Project> optProject = repository.findById(projectId);

        if(optProject.isPresent()) {
            List<Role> unfilledRoles = unfilledRoleIds.stream()
                    .map(unfilledRoleId -> Role.builder().id(unfilledRoleId).build())
                    .toList();

            List<Role> unfilledRolesFound = findUnfilledRoles(unfilledRoles);
            Project project = optProject.get();

            boolean hasFilledRole = unfilledRolesFound.stream()
                    .anyMatch(role -> isRoleFilled(project, role));

            if (hasFilledRole) throw new ConflictException("Existem cargos que já estão preenchidos");

            project.setUnfilledRoles(unfilledRolesFound);

            repository.save(project);
            return Optional.of(project);
        }

        return Optional.empty();
    }


    public boolean isNotManager(Project project, Long userId) {
        return project.getMembers().stream()
                .filter(Member::getIsManager)
                .map(member -> member.getUser().getId())
                .noneMatch(managerId -> Objects.equals(managerId, userId));
    }

    public boolean isRoleFilled(Project project, Role role) {
        return project.getMembers().stream()
                .filter(member -> member.getMemberStatus() != null
                        && member.getMemberStatus().getId() == 2L)
                .anyMatch(member -> member.getRole().equals(role));
    }

    @Transactional
    private Status findStatus(Status status) {
        return  statusService.findById(status.getId()).orElse(null);
    }

    @Transactional
    private Complexity findComplexity(Complexity complexity) {
        return complexityService.findById(complexity.getId()).orElse(null);
    }

    @Transactional
    private List<Tool> findTools(List<Tool> tools) {
        List<Tool> toolsFound = new ArrayList<>();
        tools.forEach(tool -> toolService.findById(tool.getId()).ifPresent(toolsFound::add));
        return toolsFound;
    }

    @Transactional
    private List<Topic> findTopics(List<Topic> topics) {
        List<Topic> topicsFound = new ArrayList<>();
        topics.forEach(topic -> topicService.findById(topic.getId()).ifPresent(topicsFound::add));
        return topicsFound;
    }

    @Transactional
    private List<Role> findUnfilledRoles(List<Role> unfilledRoles) {
        List<Role> unfilledRolesFound = new ArrayList<>();
        unfilledRoles.forEach(unffiledRole -> roleService.findById(unffiledRole.getId()).ifPresent(unfilledRolesFound::add));
        return unfilledRolesFound;
    }

    @Transactional
    private MemberStatus findMemberStatus(MemberStatus memberStatus) {
        return memberStatusService.findById(memberStatus.getId()).orElse(null);
    }

    @Transactional
    private User findUser(User user) {
        return userService.findById(user.getId()).orElse(null);
    }

}
