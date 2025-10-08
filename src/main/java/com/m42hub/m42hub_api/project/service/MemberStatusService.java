package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.repository.MemberStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberStatusService {

    private final MemberStatusRepository repository;

    @Transactional
    public MemberStatus save(MemberStatus memberStatus) {
        return repository.save(memberStatus);
    }

    @Transactional(readOnly = true)
    public Optional<MemberStatus> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MemberStatus> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Map<Long, MemberStatus> findAllByIds(List<Long> ids) {
        List<MemberStatus> memberStatuses = repository.findAllById(ids);
        return memberStatuses.stream().collect(Collectors.toMap(MemberStatus::getId, Function.identity()));
    }

    @Transactional(readOnly = true)
    public Optional<MemberStatus> findByName(String name) {
        return repository.findByName(name);
    }
}