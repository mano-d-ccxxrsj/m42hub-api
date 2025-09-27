package com.m42hub.m42hub_api.contribution.service;

import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.repository.ContributionRepository;
import com.m42hub.m42hub_api.contribution.repository.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository repository;

    @Transactional(readOnly = true)
    public List<Contribution> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Contribution> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public Contribution save(Contribution contribution) {
        return repository.save(contribution);
    }
}
