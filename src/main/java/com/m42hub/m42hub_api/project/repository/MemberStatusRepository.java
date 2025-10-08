package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
    Optional<MemberStatus> findByName(String name);
}