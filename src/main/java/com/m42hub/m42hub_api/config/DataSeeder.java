package com.m42hub.m42hub_api.config;

import com.m42hub.m42hub_api.exceptions.CustomNotFoundException;
import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.service.ComplexityService;
import com.m42hub.m42hub_api.project.service.ProjectService;
import com.m42hub.m42hub_api.project.service.StatusService;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.PermissionService;
import com.m42hub.m42hub_api.user.service.SystemRolePermissionService;
import com.m42hub.m42hub_api.user.service.SystemRoleService;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {
    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserService userService;
    private final StatusService statusService;
    private final ProjectService projectService;
    private final SystemRoleService systemRoleService;
    private final ComplexityService complexityService;
    private final PermissionService permissionService;
    private final SystemRolePermissionService systemRolePermissionService;

    private static final String FAKE_URL = "fake_url";
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "@admin";
    private static final String ADMIN_EMAIL = "admin@root.com";
    private static final String ADMIN_FIRST_NAME = "admin";
    private static final String ADMIN_LAST_NAME = "nimda";
    private static final String BIOGRAPHY = "Temporary fake administrator";
    private static final String DISCORD = "admin-the-fake";
    private static final String WEBSITE = "https://www.m42hub.com/";
    private static final String SYSTEM_ROLE_ADMIN = "ADMIN";
    private static final boolean IS_ACTIVE = true;

    private static final String USERNAME = "test-user";
    private static final String PASSWORD = "test-pass";
    private static final String USER_EMAIL = "test-user@test.com";
    private static final String USER_FIRST_NAME = "test";
    private static final String USER_LAST_NAME = "user";
    private static final String SYSTEM_ROLE_USER = "USER";

    private static final String PROJECT_NAME = "BabyTheRogueZila";
    private static final String PROJECT_SUMMARY = "BabyTheRogueZila is a little big project";
    private static final String PROJECT_DESCRIPTION = "Fake description";

    private static final String STATUS_NAME = "Fase de testes";
    private static final String COMPLEXITY_NAME = "Alta";

    public void createFakeData() {
        logIfAbsent("Usuário: " + USERNAME, () -> !userExists(USERNAME), this::createUserWithSystemRoleUser);
        logIfAbsent("Usuário: " + ADMIN_USERNAME, () -> !userExists(ADMIN_USERNAME), this::createUserWithSystemRoleAdmin);
        logIfAbsent("Projeto: " + PROJECT_NAME, () -> !projectExists(PROJECT_NAME), this::createProjectWithStatusAndComplexity);
        logger.info("Concluído, boa jornada!");
    }

    private void logIfAbsent(String nome, Supplier<Boolean> condition, Runnable action) {
        if (condition.get()) {
            action.run();
            logger.info("{} criado com sucesso!", nome);
            String identifier = nome.replace("Usuário:", "").replace("Projeto:", "").trim();
            if (identifier.equals(ADMIN_USERNAME)) {
                assignPermissionsToAdminRole();
                logger.info("{} CREDENTIALS: Username = {}  Password = {}", SYSTEM_ROLE_ADMIN, this.getAdminUsername(), this.getAdminPassword());
            } else if (identifier.equals(USERNAME)) {
                assignPermissionsToUserRole();
                logger.info("{} CREDENTIALS: Username = {}  Password = {}", SYSTEM_ROLE_USER, this.getUsername(), this.getPassword());
            }
        } else {
            logger.info("{} já existe, ignorando criação.", nome);
        }
    }

    private boolean userExists(String username) {
        return userService.findByUsername(username).isPresent();
    }

    private boolean projectExists(String name) {
        return projectService.findByName(name).isPresent();
    }

    public UUID createUserWithSystemRoleAdmin() {
        User user = this.createUserAdmin();
        SystemRole systemRole = this.findSystemRoleAdmin();
        user.setSystemRoleId(systemRole.getId());
        userService.saveWithRole(user);
        return user.getId();
    }

    public UUID createUserWithSystemRoleUser() {
        User user = this.createUser();
        SystemRole systemRole = this.findSystemRoleUser();
        user.setSystemRoleId(systemRole.getId());
        userService.saveWithRole(user);
        return user.getId();
    }

    public UUID createProjectWithStatusAndComplexity() {
        Project project = this.createProject();
        Complexity complexity = this.findComplexity();
        Status status = this.findStatus();
        project.setComplexityId(complexity.getId());
        project.setStatusId(status.getId());
        projectService.save(project);
        return project.getId();
    }

    public SystemRole findSystemRoleAdmin() {
        return systemRoleService.findAll().stream().filter(
                systemRole -> systemRole.getName().equalsIgnoreCase(SYSTEM_ROLE_ADMIN)
        ).findFirst().orElseThrow(() -> new CustomNotFoundException("System role não encontrado."));
    }

    public SystemRole findSystemRoleUser() {
        return systemRoleService.findAll().stream().filter(
                systemRole -> systemRole.getName().equalsIgnoreCase(SYSTEM_ROLE_USER)
        ).findFirst().orElseThrow(() -> new CustomNotFoundException("System role não encontrado."));
    }

    public Status findStatus() {
        return statusService.findAll().stream().filter(
                status -> status.getName().equalsIgnoreCase(STATUS_NAME)
        ).findFirst().orElseThrow(() -> new CustomNotFoundException("Status não foi encontrado."));
    }

    public Complexity findComplexity() {
        return complexityService.findAll().stream().filter(
                complexity -> complexity.getName().equalsIgnoreCase(COMPLEXITY_NAME)
        ).findFirst().orElseThrow(() -> new CustomNotFoundException("Complexity não encontrado."));
    }

    public void assignPermissionsToAdminRole() {
        SystemRole adminRole = systemRoleService.findAll().stream()
                .filter(r -> r.getName().equalsIgnoreCase(SYSTEM_ROLE_ADMIN))
                .findFirst()
                .orElseThrow(() -> new CustomNotFoundException("System role ADMIN não encontrado"));

        List<UUID> adminPermissionIds = permissionService.findAll().stream()
                .map(Permission::getId)
                .toList();
        systemRolePermissionService.addRelations(adminRole.getId(), adminPermissionIds);
    }

    public void assignPermissionsToUserRole() {
        SystemRole userRole = systemRoleService.findAll().stream()
                .filter(r -> r.getName().equalsIgnoreCase(SYSTEM_ROLE_USER))
                .findFirst()
                .orElseThrow(() -> new CustomNotFoundException("System role USER não encontrado"));

        List<UUID> userPermissionIds = permissionService.findAll().stream()
                .map(Permission::getId)
                .filter(id -> !idBelongsToAdmin(id))
                .toList();
        systemRolePermissionService.addRelations(userRole.getId(), userPermissionIds);
    }

    private boolean idBelongsToAdmin(UUID permissionId) {
        Permission permission = permissionService.findById(permissionId)
                .orElseThrow(() -> new CustomNotFoundException("Permissão não encontrada"));
        return permission.getName().toUpperCase().contains(SYSTEM_ROLE_ADMIN);
    }

    public User createUserAdmin() {
        User user = new User();
        user.setUsername(ADMIN_USERNAME);
        user.setPassword(ADMIN_PASSWORD);
        user.setEmail(ADMIN_EMAIL);
        user.setFirstName(ADMIN_FIRST_NAME);
        user.setLastName(ADMIN_LAST_NAME);
        user.setProfilePicUrl(FAKE_URL);
        user.setProfileBannerUrl(FAKE_URL);
        user.setBiography(BIOGRAPHY);
        user.setDiscord(DISCORD);
        user.setLinkedin(FAKE_URL);
        user.setGithub(FAKE_URL);
        user.setPersonalWebsite(WEBSITE);
        user.setIsActive(IS_ACTIVE);
        return user;
    }

    public User createUser() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setEmail(USER_EMAIL);
        user.setFirstName(USER_FIRST_NAME);
        user.setLastName(USER_LAST_NAME);
        user.setProfilePicUrl(FAKE_URL);
        user.setProfileBannerUrl(FAKE_URL);
        user.setBiography(BIOGRAPHY);
        user.setDiscord(DISCORD);
        user.setLinkedin(FAKE_URL);
        user.setGithub(FAKE_URL);
        user.setPersonalWebsite(WEBSITE);
        user.setIsActive(IS_ACTIVE);
        return user;
    }

    public Project createProject() {
        Project project = new Project();
        project.setName(PROJECT_NAME);
        project.setSummary(PROJECT_SUMMARY);
        project.setDescription(PROJECT_DESCRIPTION);
        project.setImageUrl(FAKE_URL);
        project.setStartDate(new Date());
        project.setEndDate(new Date());
        project.setDiscord(FAKE_URL);
        project.setGithub(FAKE_URL);
        project.setProjectWebsite(WEBSITE);
        return project;
    }

    public String getAdminUsername() {
        return ADMIN_USERNAME;
    }

    public String getAdminPassword() {
        return ADMIN_PASSWORD;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }
}