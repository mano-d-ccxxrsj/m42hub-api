package com.m42hub.m42hub_api.donation.repository;

import com.m42hub.m42hub_api.contribution.entity.Type;
import com.m42hub.m42hub_api.donation.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("donationPlatformRepository")
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    List<Platform> findAllByOrderByNameAsc();
}
