package com.m42hub.m42hub_api.contribution.service;

import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.contribution.repository.ContributionRepository;
import com.m42hub.m42hub_api.contribution.repository.StatusRepository;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
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
    private final UserService userService;
    private final StatusService statusService;
    private final TypeService typeService;

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
        contribution.setUser(findUser(contribution.getUser()));
        contribution.setStatus(findStatus(contribution.getStatus()));
        contribution.setType(findType(contribution.getType()));

        return repository.save(contribution);
    }

    private User findUser(User user) {
        return userService.findById(user.getId()).orElse(null);
    }

    private Status findStatus(Status status) {
        return statusService.findById(status.getId()).orElse(null);
    }

    private Type findType(Type type) {
        return typeService.findById(type.getId()).orElse(null);
    }

}
