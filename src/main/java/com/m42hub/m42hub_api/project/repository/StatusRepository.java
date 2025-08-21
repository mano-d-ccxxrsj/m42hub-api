package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Role;
import com.m42hub.m42hub_api.project.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findAllByOrderByNameAsc();
}
