package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.LoginFailAttempt;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginFailAttemptRepository extends JpaRepository<LoginFailAttempt, String> {

    Optional<LoginFailAttempt> findByEmail(String email);
}
