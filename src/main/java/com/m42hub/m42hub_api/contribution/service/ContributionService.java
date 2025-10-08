package com.m42hub.m42hub_api.contribution.service;

import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.contribution.enums.ContributionSortField;
import com.m42hub.m42hub_api.contribution.repository.ContributionRepository;
import com.m42hub.m42hub_api.contribution.specification.ContributionSpecification;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
        User foundUser = userService.findById(contribution.getUserId()).orElse(null);
        Status foundStatus = statusService.findById(contribution.getStatusId()).orElse(null);
        Type foundType = typeService.findById(contribution.getTypeId()).orElse(null);

        // Tem que tratar os null hein, ou retornar error no futuro, deixando a task ai
        contribution.setUserId(foundUser.getId());
        contribution.setStatusId(foundStatus.getId());
        contribution.setTypeId(foundType.getId());

        return repository.save(contribution);
    }

    @Transactional(readOnly = true)
    public Page<Contribution> findByParams(
            Integer page,
            Integer limit,
            String sortBy,
            String sortDirection,
            List<UUID> status,
            List<UUID> type,
            List<UUID> user,
            Date submittedAtStart,
            Date submittedAtEnd,
            Date approvedAtStart,
            Date approvedAtEnd
    ) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = ContributionSortField.APPROVED_AT.getFieldName();
        }

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        if (ContributionSortField.DESC.getFieldName().equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Contribution> spec = Specification.allOf();

        if (status != null) {
            spec = spec.and(ContributionSpecification.status(status));
        }

        if (type != null) {
            spec = spec.and(ContributionSpecification.type(type));
        }

        if (user != null) {
            spec = spec.and(ContributionSpecification.user(user));
        }

        spec = spec.and(ContributionSpecification.submittedAtBetween(submittedAtStart, submittedAtEnd));

        spec = spec.and(ContributionSpecification.approvedAtBetween(approvedAtStart, approvedAtEnd));

        return repository.findAll(spec, pageable);
    }
}