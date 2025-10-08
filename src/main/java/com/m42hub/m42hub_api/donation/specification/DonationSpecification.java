package com.m42hub.m42hub_api.donation.specification;

import com.m42hub.m42hub_api.donation.entity.Donation;
import com.m42hub.m42hub_api.donation.entity.Status;
import com.m42hub.m42hub_api.donation.entity.Type;
import com.m42hub.m42hub_api.donation.enums.DonationSortField;
import com.m42hub.m42hub_api.user.entity.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DonationSpecification {

    public static Specification<Donation> status(List<Long> statusIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Donation, Status> join = root.join(DonationSortField.STATUS.getFieldName());
            return join.get(DonationSortField.ID.getFieldName()).in(statusIds);
        };
    }

    public static Specification<Donation> type(List<Long> typeIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Donation, Type> join = root.join(DonationSortField.TYPE.getFieldName());
            return join.get(DonationSortField.ID.getFieldName()).in(typeIds);
        };
    }

    public static Specification<Donation> platform(List<Long> platformIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Donation, Type> join = root.join(DonationSortField.PLATFORM.getFieldName());
            return join.get(DonationSortField.ID.getFieldName()).in(platformIds);
        };
    }

    public static Specification<Donation> user(List<UUID> userIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Donation, User> join = root.join(DonationSortField.USER.getFieldName());
            return join.get(DonationSortField.ID.getFieldName()).in(userIds);
        };
    }

    public static Specification<Donation> donatedAtBetween(Date submittedAtStart, Date submittedAtEnd) {
        return (root, query, cb) -> {
            if (submittedAtStart != null && submittedAtEnd != null) {
                return cb.between(root.get(DonationSortField.SUBMITTED_AT.getFieldName()), submittedAtStart, submittedAtEnd);
            } else if (submittedAtStart != null) {
                return cb.greaterThanOrEqualTo(root.get(DonationSortField.SUBMITTED_AT.getFieldName()), submittedAtStart);
            } else if (submittedAtEnd != null) {
                return cb.lessThanOrEqualTo(root.get(DonationSortField.SUBMITTED_AT.getFieldName()), submittedAtEnd);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Donation> totalAmountBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            assert query != null;
            Subquery<BigDecimal> subquery = query.subquery(BigDecimal.class);
            Root<Donation> subRoot = subquery.from(Donation.class);

            subquery.select(cb.sum(subRoot.get(DonationSortField.AMOUNT.getFieldName())));
            subquery.where(cb.equal(subRoot.get(DonationSortField.USER.getFieldName()), root.get(DonationSortField.USER.getFieldName())));

            Predicate predicate = cb.conjunction();

            if (min != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(subquery.getSelection(), min));
            }
            if (max != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(subquery.getSelection(), max));
            }

            return predicate;
        };
    }
}