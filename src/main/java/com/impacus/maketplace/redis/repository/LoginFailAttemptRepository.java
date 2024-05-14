package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.LoginFailAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginFailAttemptRepository extends JpaRepository<LoginFailAttempt, String> {

    Optional<LoginFailAttempt> findByEmail(String email);
}
