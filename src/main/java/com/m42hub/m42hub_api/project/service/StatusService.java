package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository repository;

    @Transactional(readOnly = true)
    public List<Status> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Status> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Status save(Status status) {
        return repository.save(status);
    }
}
