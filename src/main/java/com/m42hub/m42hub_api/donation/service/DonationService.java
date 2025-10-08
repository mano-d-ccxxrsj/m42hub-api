package com.m42hub.m42hub_api.donation.service;

import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.enums.DonationSortField;
import com.m42hub.m42hub_api.donation.repository.DonationRepository;
import com.m42hub.m42hub_api.donation.specification.DonationSpecification;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final UserService userService;
    private final StatusService statusService;
    private final TypeService typeService;
    private final PlatformService platformService;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<Donation> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Donation> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public Donation save(Donation donation) {
        // Obs: basta bater o olho para ver que o uso de null aqui não é interessante não os tratar.
        User foundUser = userService.findById(donation.getUserId()).orElse(null);
        Status foundStatus = statusService.findById(donation.getStatusId()).orElse(null);
        Type foundType = typeService.findById(donation.getTypeId()).orElse(null);
        Platform foundPlatform = platformService.findById(donation.getPlatformId()).orElse(null);

        donation.setUserId(foundUser.getId());
        donation.setStatusId(foundStatus.getId());
        donation.setTypeId(foundType.getId());
        donation.setPlatformId(foundPlatform.getId());

        return repository.save(donation);
    }

    @Transactional(readOnly = true)
    public Page<Donation> findByParams(
            Integer page,
            Integer limit,
            String sortBy,
            String sortDirection,
            List<Long> status,
            List<Long> type,
            List<Long> platform,
            List<UUID> user,
            Date donatedAtStart,
            Date donatedAtEnd,
            BigDecimal minTotalAmount,
            BigDecimal maxTotalAmount
    ) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = DonationSortField.AMOUNT.getFieldName();
        }

        Sort sort = Sort.by(Sort.Order.desc(sortBy));
        if (DonationSortField.ASC.getFieldName().equalsIgnoreCase(sortDirection)) { // Usando o mesmo Enum feito para os projetos, 
            sort = Sort.by(Sort.Order.asc(sortBy));
        }

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Donation> spec = Specification.allOf();

        if (status != null) {
            spec = spec.and(DonationSpecification.status(status));
        }

        if (type != null) {
            spec = spec.and(DonationSpecification.type(type));
        }

        if (platform != null) {
            spec = spec.and(DonationSpecification.platform(platform));
        }

        if (user != null) {
            spec = spec.and(DonationSpecification.user(user));
        }

        spec = spec.and(DonationSpecification.donatedAtBetween(donatedAtStart, donatedAtEnd));

        spec = spec.and(DonationSpecification.totalAmountBetween(minTotalAmount, maxTotalAmount));

        return repository.findAll(spec, pageable);
    }

    //TODO: Refactor this to be more optimized, using this way just for test
    @Transactional(readOnly = true)
    public List<User> donationRanking(
            Integer limit,
            String sortDirection,
            List<Long> status,
            List<Long> type,
            List<Long> platform,
            List<UUID> userIds,
            Date donatedAtStart,
            Date donatedAtEnd,
            BigDecimal minTotalAmount,
            BigDecimal maxTotalAmount
    ) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<Donation> donation = criteriaQuery.from(Donation.class);
        Join<Donation, User> user = donation.join(DonationSortField.USER.getFieldName());

        criteriaQuery.select(user);
        criteriaQuery.groupBy(user.get(DonationSortField.ID.getFieldName()));
        criteriaQuery.orderBy(
                DonationSortField.ASC.getFieldName().equalsIgnoreCase(sortDirection)
                        ? List.of(
                        criteriaBuilder.asc(criteriaBuilder.sum(donation.get(DonationSortField.AMOUNT.getFieldName()))),
                        criteriaBuilder.desc(criteriaBuilder.max(donation.get(DonationSortField.DONATED_AT.getFieldName())))
                )
                        : List.of(
                        criteriaBuilder.desc(criteriaBuilder.sum(donation.get(DonationSortField.AMOUNT.getFieldName()))),
                        criteriaBuilder.desc(criteriaBuilder.sum(donation.get(DonationSortField.DONATED_AT.getFieldName())))
                )
        );

        List<Predicate> predicates = new ArrayList<>();
        if (status != null && !status.isEmpty()) {
            predicates.add(donation.get(DonationSortField.STATUS.getFieldName()).get(DonationSortField.ID.getFieldName()).in(status));
        }
        if (type != null && !type.isEmpty()) {
            predicates.add(donation.get(DonationSortField.TYPE.getFieldName()).get(DonationSortField.ID.getFieldName()).in(type));
        }
        if (platform != null && !platform.isEmpty()) {
            predicates.add(donation.get(DonationSortField.PLATFORM.getFieldName()).get(DonationSortField.ID.getFieldName()).in(platform));
        }
        if (userIds != null && !userIds.isEmpty()) {
            predicates.add(user.get(DonationSortField.ID.getFieldName()).in(userIds));
        }
        if (donatedAtStart != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(donation.get(DonationSortField.DONATED_AT.getFieldName()), donatedAtStart));
        }
        if (donatedAtEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(donation.get(DonationSortField.DONATED_AT.getFieldName()), donatedAtEnd));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.groupBy(user);

        List<Predicate> havingPredicates = new ArrayList<>();
        if (minTotalAmount != null) {
            havingPredicates.add(criteriaBuilder.ge(criteriaBuilder.sum(donation.get(DonationSortField.AMOUNT.getFieldName())), minTotalAmount));
        }
        if (maxTotalAmount != null) {
            havingPredicates.add(criteriaBuilder.le(criteriaBuilder.sum(donation.get(DonationSortField.AMOUNT.getFieldName())), maxTotalAmount));
        }
        if (!havingPredicates.isEmpty()) {
            criteriaQuery.having(havingPredicates.toArray(new Predicate[0]));
        }

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(limit);
        return query.getResultList();
    }
}