package com.m42hub.m42hub_api.abuse.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.entity.AbuseStatus;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;
import com.m42hub.m42hub_api.abuse.repository.AbuseRepository;
import com.m42hub.m42hub_api.abuse.specification.AbuseSpecification;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AbuseService {

    private final AbuseRepository abuseRepository;
    private final AbuseCategoryService abuseCategoryService;
    private final AbuseStatusService abuseStatusService;
    private final UserService userService;

    @Transactional
    public Abuse createAbuse(Abuse entity, Long reporterId, Long reasonCategoryId) {
        User reporter = userService.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        AbuseCategory category = abuseCategoryService.findById(reasonCategoryId);

        // Define status inicial como OPEN
        AbuseStatus openStatus = abuseStatusService.findByName("OPEN");

        entity.setReporter(reporter);
        entity.setReasonCategory(category);
        entity.setStatus(openStatus);

        return abuseRepository.save(entity);
    }

    public List<Abuse> findAll() {
        return abuseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Abuse> findByParams(
            Integer page,
            Integer limit,
            String sortBy,
            String sortDirection,
            List<String> status,
            List<TargetTypeAbuseEnum> targetType,
            List<Long> reasonCategory,
            Long reporterId,
            Long targetId) {
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "id";
        }

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        if ("DESC".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Order.desc(sortBy));
        }

        Pageable pageable = PageRequest.of(page, limit, sort);

        Specification<Abuse> spec = Specification.allOf();

        if (status != null) {
            spec = spec.and(AbuseSpecification.status(status));
        }

        if (targetType != null) {
            spec = spec.and(AbuseSpecification.targetType(targetType));
        }

        if (reasonCategory != null) {
            spec = spec.and(AbuseSpecification.reasonCategory(reasonCategory));
        }

        if (reporterId != null) {
            spec = spec.and(AbuseSpecification.reporter(reporterId));
        }

        if (targetId != null) {
            spec = spec.and(AbuseSpecification.targetId(targetId));
        }

        return abuseRepository.findAll(spec, pageable);
    }

    public Abuse findById(Long id) {
        return abuseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Denúncia não encontrada"));
    }

    @Transactional
    public Abuse updateStatus(Long id, Long status) {
        Abuse abuse = findById(id);

        AbuseStatus statusEntity = abuseStatusService.findById(status);

        abuse.setStatus(statusEntity);

        if ("CLOSED".equals(statusEntity.getName()) || "RESOLVED".equals(statusEntity.getName())) {
            abuse.setResolvedAt(LocalDateTime.now());
        }

        return abuseRepository.save(abuse);
    }

}