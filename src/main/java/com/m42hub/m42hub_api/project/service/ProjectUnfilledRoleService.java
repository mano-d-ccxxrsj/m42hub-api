package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.ProjectUnfilledRole;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.repository.ProjectUnfilledRoleRepository;
import com.m42hub.m42hub_api.project.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectUnfilledRoleService {

    private final ProjectUnfilledRoleRepository repository;
    private final RoleRepository roleRepository;

    @Transactional
    public void updateUnfilledRolesForProject(UUID projectId, List<Long> roleIds) {
        repository.deleteByProjectId(projectId);

        for (Long roleId : roleIds) {
            ProjectUnfilledRole unfilledRole = ProjectUnfilledRole.builder()
                    .projectId(projectId)
                    .roleId(roleId)
                    .build();
            repository.save(unfilledRole);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ProjectUnfilledRole> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectUnfilledRole> findAll() {
        return repository.findAll();
    }

    public List<ProjectUnfilledRole> findByProjectId(UUID projectId) {
        return repository.findByProjectId(projectId);
    }

    public List<Role> findUnfilledRolesByProjectId(UUID projectId) {
        List<ProjectUnfilledRole> unfilledRoles = repository.findByProjectId(projectId);
        List<Long> roleIds = unfilledRoles.stream()
                .map(ProjectUnfilledRole::getRoleId)
                .toList();
        return roleRepository.findAllById(roleIds);
    }

    @Transactional(readOnly = true)
    public Map<UUID, List<Role>> findUnfilledRolesByProjectIds(List<UUID> projectIds) {
        List<ProjectUnfilledRole> unfilledRoles = repository.findByProjectIdIn(projectIds);
        List<Long> roleIds = unfilledRoles.stream().map(ProjectUnfilledRole::getRoleId).distinct().toList();
        List<Role> roles = roleRepository.findAllById(roleIds);
        Map<Long, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getId, Function.identity()));

        return unfilledRoles.stream()
                .collect(Collectors.groupingBy(ProjectUnfilledRole::getProjectId,
                        Collectors.mapping(ur -> roleMap.get(ur.getRoleId()), Collectors.toList())));
    }

    // Métodos auxiliares, todos fazem parte da Transação caller, então nada de aninhar aqui com anotação de novo
    public Set<UUID> getProjectIdsByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) return Collections.emptySet();
        return repository.findByRoleIdIn(roleIds).stream()
                .map(ProjectUnfilledRole::getProjectId)
                .collect(Collectors.toSet());
    }

    public Set<Long> getRoleIdsByProject(UUID projectId) {
        return repository.findByProjectId(projectId).stream()
                .map(ProjectUnfilledRole::getRoleId)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void removeRelation(UUID projectId, Long roleId) {
        repository.deleteByProjectIdAndRoleId(projectId, roleId);
    }
}