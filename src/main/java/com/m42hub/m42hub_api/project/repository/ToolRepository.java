package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findAllByOrderByNameAsc();
}
