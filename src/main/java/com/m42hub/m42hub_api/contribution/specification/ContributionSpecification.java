package com.m42hub.m42hub_api.contribution.specification;

import com.m42hub.m42hub_api.contribution.entity.Contribution;
import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.user.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ContributionSpecification {

    public static Specification<Contribution> status (List<Long> statusIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Contribution, Status> join = root.join("status");
            return join.get("id").in(statusIds);
        };
    }

    public static Specification<Contribution> type (List<Long> typeIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Contribution, Type> join = root.join("type");
            return join.get("id").in(typeIds);
        };
    }

    public static Specification<Contribution> user (List<Long> userIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Contribution, User> join = root.join("user");
            return join.get("id").in(userIds);
        };
    }

    public static Specification<Contribution> submittedAtBetween(Date submittedAtStart, Date submittedAtEnd) {
        return (root, query, cb) -> {
            if (submittedAtStart != null && submittedAtEnd != null) {
                return cb.between(root.get("submittedAt"), submittedAtStart, submittedAtEnd);
            } else if (submittedAtStart != null) {
                return cb.greaterThanOrEqualTo(root.get("submittedAt"), submittedAtStart);
            } else if (submittedAtEnd != null) {
                return cb.lessThanOrEqualTo(root.get("submittedAt"), submittedAtEnd);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Contribution> approvedAtBetween(Date approvedAtStart, Date approvedAtEnd) {
        return (root, query, cb) -> {
            if (approvedAtStart != null && approvedAtEnd != null) {
                return cb.between(root.get("approvedAt"), approvedAtStart, approvedAtEnd);
            } else if (approvedAtStart != null) {
                return cb.greaterThanOrEqualTo(root.get("approvedAt"), approvedAtStart);
            } else if (approvedAtEnd != null) {
                return cb.lessThanOrEqualTo(root.get("approvedAt"), approvedAtEnd);
            }
            return cb.conjunction();
        };
    }


}
