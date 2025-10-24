package com.m42hub.m42hub_api.abuse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.m42hub.m42hub_api.abuse.entity.AbuseCategory;

public interface AbuseCategoryRepository extends JpaRepository<AbuseCategory, Long> {

}
