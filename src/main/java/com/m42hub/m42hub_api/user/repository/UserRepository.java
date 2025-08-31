package com.m42hub.m42hub_api.user.repository;

import com.m42hub.m42hub_api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<UserDetails> findByUsername(String username);

    Optional<User> findUserByUsername(String username);


}
