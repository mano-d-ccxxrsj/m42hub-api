package com.m42hub.m42hub_api.donation.service;

import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.repository.DonationRepository;
import com.m42hub.m42hub_api.donation.specification.DonationSpecification;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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

    @Transactional(readOnly = true)
    public Page<Donation> findByParams(
            Integer page,
            Integer limit,
            String sortBy,
            String sortDirection,
            List<Long> status,
            List<Long> type,
            List<Long> platform,
            List<Long> user,
            Date donatedAtStart,
            Date donatedAtEnd,
            BigDecimal minTotalAmount,
            BigDecimal maxTotalAmount
    ) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "amount";
        }

        Sort sort = Sort.by(Sort.Order.desc(sortBy));
        if ("ASC".equalsIgnoreCase(sortDirection)) {
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
            List<Long> userIds,
            Date donatedAtStart,
            Date donatedAtEnd,
            BigDecimal minTotalAmount,
            BigDecimal maxTotalAmount
    ) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<Donation> donation = criteriaQuery.from(Donation.class);
        Join<Donation, User> user = donation.join("user");

        criteriaQuery.select(user);
        criteriaQuery.groupBy(user.get("id"));
        criteriaQuery.orderBy(
                "ASC".equalsIgnoreCase(sortDirection)
                        ? criteriaBuilder.asc(criteriaBuilder.sum(donation.get("amount")))
                        : criteriaBuilder.desc(criteriaBuilder.sum(donation.get("amount")))
        );

        List<Predicate> predicates = new ArrayList<>();
        if (status != null && !status.isEmpty()) {
            predicates.add(donation.get("status").get("id").in(status));
        }
        if (type != null && !type.isEmpty()) {
            predicates.add(donation.get("type").get("id").in(type));
        }
        if (platform != null && !platform.isEmpty()) {
            predicates.add(donation.get("platform").get("id").in(platform));
        }
        if (userIds != null && !userIds.isEmpty()) {
            predicates.add(user.get("id").in(userIds));
        }
        if (donatedAtStart != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(donation.get("donatedAt"), donatedAtStart));
        }
        if (donatedAtEnd != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(donation.get("donatedAt"), donatedAtEnd));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        criteriaQuery.groupBy(user);

        List<Predicate> havingPredicates = new ArrayList<>();
        if (minTotalAmount != null) {
            havingPredicates.add(criteriaBuilder.ge(criteriaBuilder.sum(donation.get("amount")), minTotalAmount));
        }
        if (maxTotalAmount != null) {
            havingPredicates.add(criteriaBuilder.le(criteriaBuilder.sum(donation.get("amount")), maxTotalAmount));
        }
        if (!havingPredicates.isEmpty()) {
            criteriaQuery.having(havingPredicates.toArray(new Predicate[0]));
        }

        TypedQuery<User> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Transactional
    public Donation save(Donation donation) {
        donation.setUser(findUser(donation.getUser()));
        donation.setStatus(findStatus(donation.getStatus()));
        donation.setType(findType(donation.getType()));
        donation.setPlatform(findPlatform(donation.getPlatform()));

        return repository.save(donation);
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

    private Platform findPlatform(Platform platform) {
        return platformService.findById(platform.getId()).orElse(null);
    }


}
