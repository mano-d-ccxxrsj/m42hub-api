package com.m42hub.m42hub_api.abuse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.m42hub.m42hub_api.abuse.entity.Abuse;

public interface AbuseRepository extends JpaRepository<Abuse, Long>, JpaSpecificationExecutor<Abuse> {

}
