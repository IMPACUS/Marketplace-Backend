package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.CertificationRequestNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRequestNumberRepository extends JpaRepository<CertificationRequestNumber, String> {
}
