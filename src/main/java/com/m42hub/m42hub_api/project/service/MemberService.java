package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.*;
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
    private MemberStatus findMemberStatus(MemberStatus memberStatus) {
        return memberStatusService.findById(memberStatus.getId()).orElse(null);
    }

    @Transactional
    private User findUser(User user) {
        return userService.findById(user.getId()).orElse(null);
    }


}
