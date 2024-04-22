package com.impacus.maketplace.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.impacus.maketplace.redis.entity.EmailVerificationCode;

public interface EmailVerificationCodeRepository extends JpaRepository<EmailVerificationCode, String> {
    
}
