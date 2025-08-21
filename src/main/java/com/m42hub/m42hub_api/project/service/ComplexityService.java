package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.repository.ComplexityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ComplexityService {

    private final ComplexityRepository repository;

    @Transactional(readOnly = true)
    public List<Complexity> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Complexity> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Complexity save(Complexity status) {
        return repository.save(status);
    }

}
