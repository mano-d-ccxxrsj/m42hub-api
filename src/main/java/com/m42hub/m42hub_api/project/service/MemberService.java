package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.entity.Project;
import com.m42hub.m42hub_api.project.repository.MemberRepository;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final MemberStatusService memberStatusService;
    private final UserService userService;
    private final ProjectService projectService;

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Member save(Member member) {

        member.setMemberStatus(findMemberStatus(member.getMemberStatus()));
        member.setUser(findUser(member.getUser()));

        return repository.save(member);
    }

    @Transactional
    public Optional<Member> approve(Long memberId, Long userId) {
        Optional<Member> optMember = repository.findById(memberId);
        if (optMember.isEmpty()) return Optional.empty();

        Member member = optMember.get();

        Optional<Project> optProject = projectService.findById(member.getProject().getId());
        if (optProject.isEmpty()) return Optional.empty();

        Project project = optProject.get();

        if (projectService.isNotManager(project, userId)) return Optional.empty();

        if (projectService.isRoleFilled(project, member.getRole())) return Optional.empty();

        project.getUnfilledRoles().remove(member.getRole());

        projectService.update(project.getId(), project, userId);

        MemberStatus approvedStatus = MemberStatus.builder().id(2L).build();
        member.setMemberStatus(approvedStatus);

        repository.save(member);

        return Optional.of(member);
    }

    @Transactional
    public Optional<Member> reject(Long memberId, String applicationFeedback, Long userId) {
        Optional<Member> optMember = repository.findById(memberId);
        if (optMember.isEmpty()) return Optional.empty();

        Member member = optMember.get();

        if (member.getMemberStatus().getId() != 1L) return Optional.empty();

        Optional<Project> optProject = projectService.findById(member.getProject().getId());
        if (optProject.isEmpty()) return Optional.empty();

        Project project = optProject.get();

        if (projectService.isNotManager(project, userId)) return Optional.empty();

        MemberStatus rejectedStatus = MemberStatus.builder().id(3L).build();

        member.setMemberStatus(rejectedStatus);
        member.setApplicationFeedback(applicationFeedback);

        repository.save(member);

        return  Optional.of(member);
    }

    @Transactional
    private MemberStatus findMemberStatus(MemberStatus memberStatus) {
        return memberStatusService.findById(memberStatus.getId()).orElse(null);
    }

    @Transactional
    private User findUser(User user) {
        return userService.findById(user.getId()).orElse(null);
    }

}
