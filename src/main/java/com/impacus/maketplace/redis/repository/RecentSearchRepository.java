package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.RecentSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentSearchRepository extends JpaRepository<RecentSearch, String> {
    Optional<RecentSearch> findByUserId(String userId);
}
