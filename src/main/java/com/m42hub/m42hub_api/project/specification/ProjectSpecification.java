package com.m42hub.m42hub_api.project.specification;

import com.m42hub.m42hub_api.project.entity.*;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class ProjectSpecification {

    public static Specification<Project> status (List<Long> statusIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Project, Status> join = root.join("status");
            return join.get("id").in(statusIds);
        };
    }

    public static Specification<Project> complexity (List<Long> complexityIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Project, Complexity> join = root.join("complexity");
            return join.get("id").in(complexityIds);
        };
    }

    public static Specification<Project> tools (List<Long> toolIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Project, Tool> join = root.join("tools");
            return join.get("id").in(toolIds);
        };
    }

    public static Specification<Project> topics (List<Long> topicIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Project, Topic> join = root.join("topics");
            return join.get("id").in(topicIds);
        };
    }

    public static Specification<Project> unfilledRoles (List<Long> unfilledRoleIds) {
        return (root, query, criteriaBuilder) -> {
            Optional.ofNullable(query).ifPresent(q -> q.distinct(true));
            Join<Project, Role> join = root.join("unfilledRoles");
            return join.get("id").in(unfilledRoleIds);
        };
    }

}
