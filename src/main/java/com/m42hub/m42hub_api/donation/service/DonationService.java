package com.m42hub.m42hub_api.donation.service;

import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Platform;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.repository.DonationRepository;
import com.m42hub.m42hub_api.donation.specification.DonationSpecification;
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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final UserService userService;
    private final StatusService statusService;
    private final TypeService typeService;
    private final PlatformService platformService;


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
