package com.m42hub.m42hub_api.project.mapper;

import com.m42hub.m42hub_api.project.dto.request.MemberRequest;
import com.m42hub.m42hub_api.project.dto.response.*;
import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.user.dto.response.AuthenticatedUserResponse;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class MemberMapper {

    public static Member toMember(MemberRequest request, MemberStatus memberStatus) {
        return Member
                .builder()
                .isManager(false)
                .projectId(request.projectId())
                .roleId(request.roleId())
                .userId(request.userId())
                .statusId(memberStatus.getId())
                .applicationMessage(request.applicationMessage())
                .build();
    }

    public static Member toMemberApply(MemberRequest request, UUID userId, MemberStatus memberStatus) {
        return Member
                .builder()
                .isManager(false)
                .projectId(request.projectId())
                .roleId(request.roleId())
                .userId(userId)
                .statusId(memberStatus.getId())
                .applicationMessage(request.applicationMessage())
                .build();
    }

    public static MemberResponse toMemberResponse(
            AuthenticatedUserResponse authenticatedUserResponse,
            MemberStatusResponse memberStatusResponse,
            Member member
    ) {
        return MemberResponse
                .builder()
                .id(member.getId())
                .isManager(member.getIsManager())
                .projectId(member.getProjectId())
                .roleId(member.getRoleId())
                .user(authenticatedUserResponse)
                .memberStatus(memberStatusResponse)
                .applicationMessage(member.getApplicationMessage())
                .createdAt(member.getCreatedAt())
                .build();
    }

    public static MemberProjectResponse toMemberProjectsResponse(
            AuthenticatedUserResponse authenticatedUserResponse,
            ProjectListItemResponse projectListItemResponse,
            MemberStatusResponse memberStatusResponse,
            RoleResponse roleResponse,
            Member member
    ) {
        return MemberProjectResponse
                .builder()
                .id(member.getId())
                .isManager(member.getIsManager())
                .projectListItem(projectListItemResponse)
                .role(roleResponse)
                .user(authenticatedUserResponse)
                .memberStatus(memberStatusResponse)
                .applicationMessage(member.getApplicationMessage())
                .createdAt(member.getCreatedAt())
                .build();
    }
}