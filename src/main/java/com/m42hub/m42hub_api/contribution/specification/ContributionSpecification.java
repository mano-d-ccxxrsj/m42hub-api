package com.m42hub.m42hub_api.contribution.specification;

import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.contribution.enums.ContributionSortField;
import com.m42hub.m42hub_api.user.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ContributionSpecification {

    public static Specification<Contribution> status(List<UUID> statusIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Contribution, Status> join = root.join(ContributionSortField.STATUS.getFieldName());
            return join.get(ContributionSortField.ID.getFieldName()).in(statusIds);
        };
    }

    public static Specification<Contribution> type(List<UUID> typeIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Contribution, Type> join = root.join(ContributionSortField.TYPE.getFieldName());
            return join.get(ContributionSortField.ID.getFieldName()).in(typeIds);
        };
    }

    public static Specification<Contribution> user(List<UUID> userIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Contribution, User> join = root.join(ContributionSortField.USER.getFieldName());
            return join.get(ContributionSortField.ID.getFieldName()).in(userIds);
        };
    }

    public static Specification<Contribution> submittedAtBetween(Date submittedAtStart, Date submittedAtEnd) {
        return (root, query, cb) -> {
            if (submittedAtStart != null && submittedAtEnd != null) {
                return cb.between(root.get(ContributionSortField.SUBMITTED_AT.getFieldName()), submittedAtStart, submittedAtEnd);
            } else if (submittedAtStart != null) {
                return cb.greaterThanOrEqualTo(root.get(ContributionSortField.SUBMITTED_AT.getFieldName()), submittedAtStart);
            } else if (submittedAtEnd != null) {
                return cb.lessThanOrEqualTo(root.get(ContributionSortField.SUBMITTED_AT.getFieldName()), submittedAtEnd);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Contribution> approvedAtBetween(Date approvedAtStart, Date approvedAtEnd) {
        return (root, query, cb) -> {
            if (approvedAtStart != null && approvedAtEnd != null) {
                return cb.between(root.get(ContributionSortField.APPROVED_AT.getFieldName()), approvedAtStart, approvedAtEnd);
            } else if (approvedAtStart != null) {
                return cb.greaterThanOrEqualTo(root.get(ContributionSortField.APPROVED_AT.getFieldName()), approvedAtStart);
            } else if (approvedAtEnd != null) {
                return cb.lessThanOrEqualTo(root.get(ContributionSortField.APPROVED_AT.getFieldName()), approvedAtEnd);
            }
            return cb.conjunction();
        };
    }
}