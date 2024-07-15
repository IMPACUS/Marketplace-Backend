package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.EmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, String> {
    Optional<EmailVerificationCode> findByEmail(String email);

    Optional<EmailVerificationCode> findByEmailAndCode(String email, String code);
}
