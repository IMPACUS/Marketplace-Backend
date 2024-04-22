package com.impacus.maketplace.redis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.impacus.maketplace.redis.entity.EmailVerificationCode;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, String> {
    Optional<EmailVerificationCode> findByEmail(String email);

    Optional<EmailVerificationCode> findByEmailAndCode(String email, String code);
}
