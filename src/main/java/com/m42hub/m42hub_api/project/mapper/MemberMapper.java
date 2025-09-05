package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MemberMapper {

    public static Member toMember(MemberRequest request) {

        Project project = Project.builder().id(request.projectId()).build();
        Role role = Role.builder().id(request.roleId()).build();
        User user = User.builder().id(request.userId()).build();
        MemberStatus memberStatus = MemberStatus.builder().id(1L).build();

        return Member
                .builder()
                .isManager(false)
                .project(project)
                .role(role)
                .user(user)
                .memberStatus(memberStatus)
                .applicationMessage(request.applicationMessage())
                .build();
    }

    public static Member toMemberApply(MemberRequest request, Long userId) {

        Project project = Project.builder().id(request.projectId()).build();
        Role role = Role.builder().id(request.roleId()).build();
        User user = User.builder().id(userId).build();
        MemberStatus memberStatus = MemberStatus.builder().id(1L).build();

        return Member
                .builder()
                .isManager(false)
                .project(project)
                .role(role)
                .user(user)
                .memberStatus(memberStatus)
                .applicationMessage(request.applicationMessage())
                .build();
    }

    public static MemberResponse toMemberResponse(Member member) {

        AuthenticatedUserResponse user = member.getUser() != null ? UserMapper.toAuthenticatedUserResponse(member.getUser()) : null;
        MemberStatusResponse memberStatus = member.getUser() != null ? MemberStatusMapper.toMemberStatusResponse(member.getMemberStatus()) : null;

        return MemberResponse
                .builder()
                .id(member.getId())
                .isManager(member.getIsManager())
                .projectId(member.getProject().getId())
                .roleId(member.getRole().getId())
                .user(user)
                .memberStatus(memberStatus)
                .applicationMessage(member.getApplicationMessage())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberProjectResponse toMemberProjectsResponse(Member member) {

        AuthenticatedUserResponse user = member.getUser() != null ? UserMapper.toAuthenticatedUserResponse(member.getUser()) : null;
        MemberStatusResponse memberStatus = member.getUser() != null ? MemberStatusMapper.toMemberStatusResponse(member.getMemberStatus()) : null;
        ProjectListItemResponse projectListItem = member.getProject() != null ? ProjectMapper.toProjectListResponse(member.getProject()) : null;
        RoleResponse role = member.getRole() != null ? RoleMapper.toRoleResponse(member.getRole()) : null;

        return MemberProjectResponse
                .builder()
                .id(member.getId())
                .isManager(member.getIsManager())
                .projectListItem(projectListItem)
                .role(role)
                .user(user)
                .memberStatus(memberStatus)
                .applicationMessage(member.getApplicationMessage())
                .createdAt(member.getCreatedAt())
                .build();
    }

}
