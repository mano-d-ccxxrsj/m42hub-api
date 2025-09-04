package com.m42hub.m42hub_api.project.repository;

import com.m42hub.m42hub_api.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findAllByUserId(Long userId);

}
