package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.repository.ComplexityRepository;
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
public class ComplexityService {

    private final ComplexityRepository repository;

    @Transactional
    public Complexity save(Complexity status) {
        return repository.save(status);
    }

    @Transactional(readOnly = true)
    public Optional<Complexity> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Complexity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Map<Long, Complexity> findAllByIds(List<Long> ids) {
        List<Complexity> complexities = repository.findAllById(ids);
        return complexities.stream().collect(Collectors.toMap(Complexity::getId, Function.identity()));
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long complexityId) {
        return repository.existsById(complexityId);
    }
}