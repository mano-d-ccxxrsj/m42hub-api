package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUserId(UUID userId);
    List<Member> findAllByProjectId(UUID projectId);
    List<Member> findByProjectIdIn(List<UUID> projectIds);
    Optional<Member> findByProjectIdAndUserId(UUID projectId, UUID userId);
}