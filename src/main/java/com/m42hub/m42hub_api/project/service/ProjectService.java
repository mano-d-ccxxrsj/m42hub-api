package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.file.service.ImgBBService;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.enums.ProjectSortField;
import com.m42hub.m42hub_api.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectUnfilledRoleService projectUnfilledRoleService;
    private final ProjectTopicService projectTopicService;
    private final ProjectToolService projectToolService;
    private final ProjectRepository projectRepository;
    private final ComplexityService complexityService;
    private final StatusService statusService;
    private final MemberService memberService;
    private final ImgBBService imgBBService;

    @Transactional
    public Project save(Project project) {
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Optional<Project> findById(UUID id) {
        return projectRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Project> findByName(String name) {
        return projectRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Map<UUID, Project> findAllByIds(List<UUID> projectIds) {
        List<Project> projects = projectRepository.findAllById(projectIds);
        return projects.stream().collect(Collectors.toMap(Project::getId, Function.identity()));
    }

    // Esta função precisa sofrer testes rigorosos, pois me deu trabalho de mais.
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

        Set<UUID> filteredIds = this.applyAllFilters(status, complexity, tools, topics, unfilledRoles);
        List<Project> filteredProjects = projectRepository.findAllById(filteredIds);

        List<Project> sortedProjects = this.sortProjects(filteredProjects, sortBy, sortDirection);
        return this.getPaginatedResults(sortedProjects, PageRequest.of(page, limit));
    }

    @Transactional
    public Optional<Project> update(UUID projectId, Project updatedProject, UUID userId) {
        return projectRepository.findById(projectId).map(project -> {
            if (!memberService.isUserProjectManager(projectId, userId)) {
                throw new RuntimeException("Usuário não tem permissão para alterar o projeto");
            }

            Optional.ofNullable(updatedProject.getName()).ifPresent(project::setName);
            Optional.ofNullable(updatedProject.getSummary()).ifPresent(project::setSummary);
            Optional.ofNullable(updatedProject.getDescription()).ifPresent(project::setDescription);
            Optional.ofNullable(updatedProject.getImageUrl()).ifPresent(project::setImageUrl);
            Optional.ofNullable(updatedProject.getStartDate()).ifPresent(project::setStartDate);
            Optional.ofNullable(updatedProject.getEndDate()).ifPresent(project::setEndDate);
            Optional.ofNullable(updatedProject.getDiscord()).ifPresent(project::setDiscord);
            Optional.ofNullable(updatedProject.getGithub()).ifPresent(project::setGithub);
            Optional.ofNullable(updatedProject.getProjectWebsite()).ifPresent(project::setProjectWebsite);
            Optional.ofNullable(updatedProject.getStatusId()).filter(statusService::existsById).ifPresent(project::setStatusId);
            Optional.ofNullable(updatedProject.getComplexityId()).filter(complexityService::existsById).ifPresent(project::setComplexityId);

            return projectRepository.save(project);
        });
    }

    // Aqui não vi sentido em retornar o Project, mas por compatibilidade, mantido o retorno.
    // Primeiro intuito é refatorar, não mudar a lógica.
    @Transactional
    public Optional<Project> changeUnfilledRoles(UUID projectId, List<Long> unfilledRoles, UUID userId) {
        return projectRepository.findById(projectId).map(project -> {

            if (!memberService.isUserProjectManager(projectId, userId)) {
                throw new RuntimeException("Usuário não tem permissão para alterar funções não preenchidas");
            }

            projectUnfilledRoleService.updateUnfilledRolesForProject(projectId, unfilledRoles);
            return project;
        });
    }

    @Transactional
    public Optional<Project> changeProjectBanner(MultipartFile file, UUID projectId, UUID userId) {
        return projectRepository.findById(projectId).map(project -> {
            String imageUrl = imgBBService.uploadImage(file);

            if (!memberService.isUserProjectManager(projectId, userId)) {
                throw new RuntimeException("Usuário que solicitou alteração não é o idealizador do projeto");
            }

            project.setImageUrl(imageUrl);
            return projectRepository.save(project);
        });
    }

    // Métodos auxiliares, todos fazem parte da Transação caller, então nada de aninhar aqui com anotação de novo
    private Set<UUID> applyAllFilters(
            List<Long> status,
            List<Long> complexity,
            List<Long> tools,
            List<Long> topics,
            List<Long> unfilledRoles
    ) {

        List<Set<UUID>> allFilters = List.of(
                getProjectIdsByStatusIds(status),
                getProjectIdsByComplexityIds(complexity),
                projectToolService.getProjectIdsByToolIds(tools),
                projectTopicService.getProjectIdsByTopicIds(topics),
                projectUnfilledRoleService.getProjectIdsByRoleIds(unfilledRoles)
        );

        List<Set<UUID>> nonEmptyFilters = allFilters.stream()
                .filter(set -> !set.isEmpty())
                .toList();

        if (nonEmptyFilters.isEmpty()) {
            return getAllProjectIds();
        }

        return nonEmptyFilters.stream()
                .reduce(this::intersect)
                .orElse(Collections.emptySet());
    }

    private Set<UUID> getProjectIdsByStatusIds(List<Long> statusIds) {
        if (statusIds == null || statusIds.isEmpty()) return Collections.emptySet();
        return projectRepository.findByStatusIdIn(statusIds).stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
    }

    private Set<UUID> getProjectIdsByComplexityIds(List<Long> complexityIds) {
        if (complexityIds == null || complexityIds.isEmpty()) return Collections.emptySet();
        return projectRepository.findByComplexityIdIn(complexityIds).stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
    }

    private Set<UUID> getAllProjectIds() {
        return projectRepository.findAll().stream()
                .map(Project::getId)
                .collect(Collectors.toSet());
    }

    private Set<UUID> intersect(Set<UUID> set1, Set<UUID> set2) {
        Set<UUID> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        return intersection;
    }

    private List<Project> sortProjects(List<Project> projects, String sortBy, String sortDirection) {
        ProjectSortField sortField = ProjectSortField.fromString(sortBy);
        boolean ascending = !ProjectSortField.DESC.getFieldName().equalsIgnoreCase(sortDirection);

        Comparator<Project> comparator = switch (sortField) {
            case NAME -> Comparator.comparing(Project::getName);
            case CREATED_AT -> Comparator.comparing(Project::getCreatedAt);
            case UPDATED_AT -> Comparator.comparing(Project::getUpdatedAt);
            case START_DATE -> Comparator.comparing(Project::getStartDate);
            case END_DATE -> Comparator.comparing(Project::getEndDate);
            default -> Comparator.comparing(Project::getId);
        };

        return ascending ?
                projects.stream().sorted(comparator).collect(Collectors.toList()) :
                projects.stream().sorted(comparator.reversed()).collect(Collectors.toList());
    }

    private Page<Project> getPaginatedResults(List<Project> projects, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        if (projects.size() < startItem) {
            return new PageImpl<>(List.of(), pageable, projects.size());
        }

        int toIndex = Math.min(startItem + pageSize, projects.size());
        List<Project> pageContent = projects.subList(startItem, toIndex);

        return new PageImpl<>(pageContent, pageable, projects.size());
    }
}