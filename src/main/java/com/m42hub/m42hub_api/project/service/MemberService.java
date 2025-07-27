package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

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
        return repository.save(member);
    }

}
