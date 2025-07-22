package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Complexity;
import com.m42hub.m42hub_api.project.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplexityRepository extends JpaRepository<Complexity, Long> {
}
