package com.m42hub.m42hub_api.abuse.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.m42hub.m42hub_api.abuse.entity.Abuse;
import com.m42hub.m42hub_api.abuse.enums.TargetTypeAbuseEnum;

public class AbuseSpecification {

    public static Specification<Abuse> status(List<String> statuses) {
        return (root, query, criteriaBuilder) -> {
            if (statuses == null || statuses.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("status").get("name").in(statuses);
        };
    }

    public static Specification<Abuse> targetType(List<TargetTypeAbuseEnum> targetTypes) {
        return (root, query, criteriaBuilder) -> {
            if (targetTypes == null || targetTypes.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("targetType").in(targetTypes);
        };
    }

    public static Specification<Abuse> reasonCategory(List<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("reasonCategory").get("id").in(categoryIds);
        };
    }

    public static Specification<Abuse> reporter(Long reporterId) {
        return (root, query, criteriaBuilder) -> {
            if (reporterId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("reporter").get("id"), reporterId);
        };
    }

    public static Specification<Abuse> targetId(Long targetId) {
        return (root, query, criteriaBuilder) -> {
            if (targetId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("targetId"), targetId);
        };
    }
}