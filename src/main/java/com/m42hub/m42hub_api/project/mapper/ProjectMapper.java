package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.ProjectRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectUpdateRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.Project;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ProjectMapper {

    public static Project toProject(ProjectRequest request) {
        return Project.builder()
                .name(request.name())
                .summary(request.summary())
                .description(request.description())
                .statusId(request.statusId())
                .complexityId(request.complexityId())
                .imageUrl(request.imageUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .discord(request.discord())
                .github(request.github())
                .projectWebsite(request.projectWebsite())
                .build();
    }

    public static Project toUpdatedProject(ProjectUpdateRequest request) {
        return Project.builder()
                .name(request.name())
                .summary(request.summary())
                .description(request.description())
                .statusId(request.statusId())
                .complexityId(request.complexityId())
                .imageUrl(request.imageUrl())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .discord(request.discord())
                .github(request.github())
                .projectWebsite(request.projectWebsite())
                .build();
    }

    public static ProjectResponse toProjectResponse(
            Project project,
            StatusResponse status,
            ComplexityResponse complexity,
            List<ToolResponse> tools,
            List<TopicResponse> topics,
            List<RoleResponse> unfilledRoles,
            List<MemberResponse> members
    ) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .summary(project.getSummary())
                .description(project.getDescription())
                .status(status)
                .imageUrl(project.getImageUrl())
                .complexity(complexity)
                .creationDate(project.getCreatedAt())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .tools(tools)
                .topics(topics)
                .unfilledRoles(unfilledRoles)
                .members(members)
                .discord(project.getDiscord())
                .github(project.getGithub())
                .projectWebsite(project.getProjectWebsite())
                .build();
    }

    public static ProjectListItemResponse toProjectListResponse(
            Project project,
            String statusName,
            String complexityName,
            List<String> toolNames,
            List<String> topicNames,
            List<String> unfilledRoleNames,
            String manager
    ) {
        return ProjectListItemResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .summary(project.getSummary())
                .statusName(statusName)
                .imageUrl(project.getImageUrl())
                .complexityName(complexityName)
                .creationDate(project.getCreatedAt())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .toolNames(toolNames)
                .topicNames(topicNames)
                .unfilledRoleNames(unfilledRoleNames)
                .manager(manager)
                .discord(project.getDiscord())
                .github(project.getGithub())
                .projectWebsite(project.getProjectWebsite())
                .build();
    }
}