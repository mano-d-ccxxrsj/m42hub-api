package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.project.repository.ProjectRepository;
import com.m42hub.m42hub_api.project.specification.ProjectSpecification;
import com.m42hub.m42hub_api.user.entity.SystemRole;
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
    private final MemberService memberService;


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
        project.setStatus(findStatus(project.getStatus()));
        project.setComplexity(findComplexity(project.getComplexity()));
        project.setTools(findTools(project.getTools()));
        project.setTopics(findTopics(project.getTopics()));
        project.setUnfilledRoles(findUnfilledRoles(project.getUnfilledRoles()));

        return repository.save(project);
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

}
