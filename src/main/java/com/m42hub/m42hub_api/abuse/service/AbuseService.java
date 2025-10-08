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

import com.m42hub.m42hub_api.abuse.dto.request.AbuseRequest;
import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;
import com.m42hub.m42hub_api.abuse.enums.AbuseStatusEnum;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;
import com.m42hub.m42hub_api.abuse.mapper.AbuseMapper;
import com.m42hub.m42hub_api.abuse.repository.AbuseCategoryRepository;
import com.m42hub.m42hub_api.abuse.repository.AbuseRepository;
import com.m42hub.m42hub_api.abuse.specification.AbuseSpecification;
import com.m42hub.m42hub_api.user.entity.User;
import com.m42hub.m42hub_api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AbuseService {

    private final AbuseRepository abuseRepository;
    private final AbuseCategoryRepository abuseCategoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public Abuse createAbuse(AbuseRequest request, Long reporterId) {
        User reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        AbuseCategory category = abuseCategoryRepository.findById(request.reasonCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Abuse abuse = AbuseMapper.toAbuse(request, reporter, category);

        return abuseRepository.save(abuse);
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
    public Abuse updateStatus(Long id, AbuseStatusEnum status) {
        Abuse abuse = findById(id);
        abuse.setStatus(status);

        if (status.equals(AbuseStatusEnum.CLOSED) || status.equals(AbuseStatusEnum.RESOLVED)) {
            abuse.setResolvedAt(LocalDateTime.now());
        }

        return abuseRepository.save(abuse);
    }

}