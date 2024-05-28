package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.ExternalAPIToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalAPITokenRepository extends JpaRepository<ExternalAPIToken, String> {
}
