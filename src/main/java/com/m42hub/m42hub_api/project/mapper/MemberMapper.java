package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import com.m42hub.m42hub_api.user.dto.response.UserResponse;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.mapper.UserMapper;
import lombok.experimental.UtilityClass;

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
        return MemberResponse
                .builder()
                .id(member.getId())
                .isManager(member.getIsManager())
                .project(member.getProject().getId())
                .role(member.getRole().getId())
                .user(member.getUser().getId())
                .build();
    }

}
