package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Member;
import com.m42hub.m42hub_api.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
