package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.CertificationRequestNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificationRequestNumberRepository extends JpaRepository<CertificationRequestNumber, String> {
    // reqNumber로 존재 여부 확인
    boolean existsByReqNumber(String reqNumber);

    // reqNumber로 엔티티 조회
    Optional<CertificationRequestNumber> findByReqNumber(String reqNumber);
}
