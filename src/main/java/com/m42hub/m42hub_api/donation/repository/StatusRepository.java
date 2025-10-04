package com.m42hub.m42hub_api.donation.repository;

import com.m42hub.m42hub_api.donation.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("donationStatusRepository")
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findAllByOrderByNameAsc();
}
