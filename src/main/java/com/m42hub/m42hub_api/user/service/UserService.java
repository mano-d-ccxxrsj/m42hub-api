package com.m42hub.m42hub_api.user.service;

import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.entity.Tool;
import com.m42hub.m42hub_api.project.service.RoleService;
import com.m42hub.m42hub_api.user.dto.request.UserInfoRequest;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final SystemRoleService systemRoleService;
    private final RoleService projectRoleService;


    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setSystemRole(findSystemRole(user.getSystemRole()));
        return repository.save(user);
    }

    @Transactional
    public Optional<User> editInfo(UserInfoRequest updatedUserInfo, Long userId) {
        Optional<User> optUser = repository.findById(userId);
        if(optUser.isPresent()) {
            User user = optUser.get();

            if (updatedUserInfo.firstName() != null) user.setFirstName(updatedUserInfo.firstName());
            if (updatedUserInfo.lastName() != null) user.setLastName(updatedUserInfo.lastName());
            if (updatedUserInfo.biography() != null) user.setBiography(updatedUserInfo.biography());
            if (updatedUserInfo.discord() != null) user.setDiscord(updatedUserInfo.discord());
            if (updatedUserInfo.linkedin() != null) user.setLinkedin(updatedUserInfo.linkedin());
            if (updatedUserInfo.github() != null) user.setGithub(updatedUserInfo.github());
            if (updatedUserInfo.personalWebsite() != null) user.setPersonalWebsite(updatedUserInfo.personalWebsite());



            if (updatedUserInfo.interestRoles() != null) {
                user.setInterestRoles(findProjectRoles(updatedUserInfo.interestRoles()));
            }


            repository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Transactional
    private SystemRole findSystemRole(SystemRole systemRole) {
        return systemRoleService.findById(systemRole.getId()).orElse(null);
    }

    @Transactional
    private List<Role> findProjectRoles(List<Long> projectRoles) {
        List<Role> projectRolesFound = new ArrayList<>();
        projectRoles.forEach(roleId -> projectRoleService.findById(roleId).ifPresent(projectRolesFound::add));
        return projectRolesFound;
    }

}
