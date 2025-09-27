package com.m42hub.m42hub_api.contribution.repository;

import com.m42hub.m42hub_api.contribution.entity.Status;
import com.m42hub.m42hub_api.contribution.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("contributionTypeRepository")
public interface TypeRepository extends JpaRepository<Type, Long> {
    List<Type> findAllByOrderByNameAsc();
}
