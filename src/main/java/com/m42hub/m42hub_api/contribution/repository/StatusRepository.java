package com.m42hub.m42hub_api.contribution.repository;

import com.m42hub.m42hub_api.contribution.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("contributionStatusRepository")
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findAllByOrderByNameAsc();
}
