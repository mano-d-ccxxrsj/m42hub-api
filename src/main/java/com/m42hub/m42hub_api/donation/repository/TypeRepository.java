package com.m42hub.m42hub_api.donation.repository;

import com.m42hub.m42hub_api.donation.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("donationTypeRepository")
public interface TypeRepository extends JpaRepository<Type, Long> {
    List<Type> findAllByOrderByNameAsc();
}
