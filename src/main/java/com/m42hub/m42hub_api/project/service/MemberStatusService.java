package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.MemberStatus;
import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.repository.MemberStatusRepository;
import com.m42hub.m42hub_api.project.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberStatusService {

    private final MemberStatusRepository repository;

    @Transactional(readOnly = true)
    public List<MemberStatus> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MemberStatus> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public MemberStatus save(MemberStatus memberStatus) {
        return repository.save(memberStatus);
    }
}
