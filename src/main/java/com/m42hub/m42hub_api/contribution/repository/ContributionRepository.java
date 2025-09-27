package com.m42hub.m42hub_api.contribution.repository;

import com.m42hub.m42hub_api.contribution.entity.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, UUID>, JpaSpecificationExecutor<Contribution> {
}
