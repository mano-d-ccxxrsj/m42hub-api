package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Status;
import com.m42hub.m42hub_api.project.repository.StatusRepository;
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
public class StatusService {

    private final StatusRepository repository;

    @Transactional
    public Status save(Status status) {
        return repository.save(status);
    }

    @Transactional(readOnly = true)
    public Optional<Status> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Status> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Map<Long, Status> findAllByIds(List<Long> ids) {
        List<Status> statuses = repository.findAllById(ids);
        return statuses.stream().collect(Collectors.toMap(Status::getId, Function.identity()));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long statusId) {
        return repository.existsById(statusId);
    }
}