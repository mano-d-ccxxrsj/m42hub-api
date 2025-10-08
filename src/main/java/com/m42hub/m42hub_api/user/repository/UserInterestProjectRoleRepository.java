package com.m42hub.m42hub_api.user.repository;

import com.m42hub.m42hub_api.user.entity.UserInterestProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserInterestProjectRoleRepository extends JpaRepository<UserInterestProjectRole, Long> {
    List<UserInterestProjectRole> findByUserId(UUID userId);
    List<UserInterestProjectRole> findByRoleId(Long roleId);
    void deleteByUserIdAndRoleId(UUID userId, Long roleId);
}