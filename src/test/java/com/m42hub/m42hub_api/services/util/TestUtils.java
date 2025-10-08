package com.m42hub.m42hub_api.services.util;

import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.user.entity.Permission;
import com.m42hub.m42hub_api.user.entity.SystemRole;
import com.m42hub.m42hub_api.user.entity.User;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class TestUtils {

    public static UUID getRandomUUID() {
        return UUID.randomUUID();
    }

    public static SystemRole createRole(UUID id, String name) {
        return SystemRole.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static User createUser(
            UUID id,
            String username,
            String firstName,
            String lastName,
            String email,
            String password,
            String profilePicUrl,
            String profileBannerUrl,
            String biography,
            String discord,
            String linkedin,
            String github,
            String personalWebsite,
            Boolean isActive,
            LocalDateTime lastLogin,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return User.builder()
                .id(id)
                .username(username)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .profilePicUrl(profilePicUrl)
                .profileBannerUrl(profileBannerUrl)
                .biography(biography)
                .discord(discord)
                .linkedin(linkedin)
                .github(github)
                .personalWebsite(personalWebsite)
                .isActive(isActive)
                .lastLogin(lastLogin)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static Permission createPermission(UUID id, String name, String description) {
        return Permission.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static ProjectTopic createProjectTopic(Long id, UUID projectId, Long topicId) {
        return ProjectTopic.builder()
                .id(id)
                .projectId(projectId)
                .topicId(topicId)
                .build();
    }

    public static Topic createTopic(Long id, String name, String hexColor, String description) {
        return Topic.builder()
                .id(id)
                .name(name)
                .hexColor(hexColor)
                .description(description)
                .build();
    }

    public static ProjectTool createProjectTool(Long id, UUID projectId, Long toolId) {
        return ProjectTool.builder()
                .id(id)
                .projectId(projectId)
                .toolId(toolId)
                .build();
    }

    public static Tool createTool(Long id, String name, String hexColor, String description) {
        return Tool.builder()
                .id(id)
                .name(name)
                .hexColor(hexColor)
                .description(description)
                .build();
    }

    public static Status createStatus(Long id, String name, String description) {
        return Status.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static Role createRole(Long id, String name, String description) {
        return Role.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static MemberStatus createMemberStatus(Long id, String name, String description) {
        return MemberStatus.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static Member createMember(
            Long id,
            Boolean isManager,
            String applicationMessage,
            String applicationFeedback
    ) {
        return Member.builder()
                .id(id)
                .isManager(isManager)
                .applicationMessage(applicationMessage)
                .applicationFeedback(applicationFeedback)
                .build();
    }

    public static Project createProject(
            UUID id,
            String name,
            String summary,
            String description,
            Long statusId,
            Long complexityId,
            String imageUrl,
            Date startDate,
            Date endDate,
            String discord,
            String github,
            String projectWebsite,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return Project.builder()
                .id(id)
                .name(name)
                .summary(summary)
                .description(description)
                .statusId(statusId)
                .complexityId(complexityId)
                .imageUrl(imageUrl)
                .startDate(startDate)
                .endDate(endDate)
                .discord(discord)
                .github(github)
                .projectWebsite(projectWebsite)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public static Complexity createComplexity(Long id, String name, String hexColor, String description) {
        return Complexity.builder()
                .id(id)
                .name(name)
                .hexColor(hexColor)
                .description(description)
                .build();
    }
}