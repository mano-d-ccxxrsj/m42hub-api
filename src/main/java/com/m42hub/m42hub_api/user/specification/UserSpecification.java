package com.m42hub.m42hub_api.user.specification;

import com.m42hub.m42hub_api.project.entity.*;
import com.m42hub.m42hub_api.user.entity.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class UserSpecification {

    public static Specification<User> interestRoles (List<Long> interestRolesIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<User, Role> join = root.join("interestRoles");
            return join.get("id").in(interestRolesIds);
        };
    }

}
