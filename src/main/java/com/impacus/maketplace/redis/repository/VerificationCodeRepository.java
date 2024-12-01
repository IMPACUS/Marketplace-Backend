package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, String> {
    Optional<VerificationCode> findByIdentifier(String identifier);

    Optional<VerificationCode> findByIdentifierAndCode(String identifier, String code);
}
