package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.request.ProjectRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class MemberMapper {

    public static Member toMember(MemberRequest request) {

        Project project = Project.builder().id(request.projectId()).build();
        Role role = Role.builder().id(request.roleId()).build();
        User user = User.builder().id(request.userId()).build();


        return Member
                .builder()
                .isManager(request.isManager())
                .project(project)
                .role(role)
                .user(user)
                .build();
    }

    public static MemberResponse toMemberResponse(Member member) {

        ProjectResponse project = member.getProject() != null ? ProjectMapper.toProjectResponse(member.getProject()) : null;
        RoleResponse role = member.getRole() != null ? RoleMapper.toRoleResponse(member.getRole()) : null;
        UserResponse user = member.getRole() != null ? UserMapper.toUserResponse(member.getUser()) : null;

        return MemberResponse
                .builder()
                .id(member.getId())
                .isManager(member.getIsManager())
                .project(project)
                .role(role)
                .user(user)
                .build();
    }

}
