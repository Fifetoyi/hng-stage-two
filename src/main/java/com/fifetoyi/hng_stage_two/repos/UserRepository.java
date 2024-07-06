package com.fifetoyi.hng_stage_two.repos;

import com.fifetoyi.hng_stage_two.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<User> findByUserId(String userId);
}
