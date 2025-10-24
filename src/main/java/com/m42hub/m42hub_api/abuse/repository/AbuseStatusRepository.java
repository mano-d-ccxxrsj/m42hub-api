package com.m42hub.m42hub_api.abuse.repository;

import com.m42hub.m42hub_api.abuse.entity.AbuseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbuseStatusRepository extends JpaRepository<AbuseStatus, Long> {
    
    Optional<AbuseStatus> findByName(String name);
}