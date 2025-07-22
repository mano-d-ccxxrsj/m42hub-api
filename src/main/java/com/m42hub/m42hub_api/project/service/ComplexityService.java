package com.m42hub.m42hub_api.project.service;

import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.repository.ComplexityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ComplexityService {

    private final ComplexityRepository repository;

    public List<Complexity> findAll() {
        return repository.findAll();
    }

    public Optional<Complexity> findById(Long id) {
        return repository.findById(id);
    }

    public Complexity save(Complexity status) {
        return repository.save(status);
    }

}
