package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.repository.RoleRepository;
import com.m42hub.m42hub_api.user.entity.UserInterestProjectRole;
import com.m42hub.m42hub_api.user.repository.UserInterestProjectRoleRepository;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserInterestProjectRoleService {

    private final UserInterestProjectRoleRepository userInterestRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void addRelation(UUID userId, Long roleId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User não existe");
        }
        if (!roleRepository.existsById(roleId)) {
            throw new IllegalArgumentException("Role não existe");
        }

        UserInterestProjectRole relation = UserInterestProjectRole.builder()
                .userId(userId)
                .roleId(roleId)
                .build();

        userInterestRoleRepository.save(relation);
    }

    @Transactional(readOnly = true)
    public Optional<UserInterestProjectRole> findById(Long id) {
        return userInterestRoleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<UserInterestProjectRole> findAll() {
        return userInterestRoleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<UUID> getUsersByRole(Long roleId) {
        return userInterestRoleRepository.findByRoleId(roleId).stream()
                .map(UserInterestProjectRole::getUserId)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Role> getRolesByUser(UUID userId) {
        List<Long> roleIds = userInterestRoleRepository.findByUserId(userId).stream()
                .map(UserInterestProjectRole::getRoleId)
                .toList();

        return roleRepository.findAllById(roleIds);
    }

    @Transactional
    public void removeRelation(UUID userId, Long roleId) {
        userInterestRoleRepository.deleteByUserIdAndRoleId(userId, roleId);
    }

    // Métodos auxiliares, todos fazem parte da Transação caller, então nada de aninhar aqui com anotação de novo
    public void addRelations(UUID userId, List<Long> roleIds) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User não existe");
        }

        List<Role> validRoles = roleRepository.findAllById(roleIds);
        List<Long> validRoleIds = validRoles.stream()
                .map(Role::getId)
                .toList();

        List<UserInterestProjectRole> relations = validRoleIds.stream()
                .map(roleId -> UserInterestProjectRole.builder()
                        .userId(userId)
                        .roleId(roleId)
                        .build())
                .toList();

        if (!relations.isEmpty()) {
            userInterestRoleRepository.saveAll(relations);
        }
    }
}