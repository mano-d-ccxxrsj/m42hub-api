package com.m42hub.m42hub_api.abuse.repository;

import com.m42hub.m42hub_api.abuse.entity.UserFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserFlagRepository extends JpaRepository<UserFlag, UUID> {}