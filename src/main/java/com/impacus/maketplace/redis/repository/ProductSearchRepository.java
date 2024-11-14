package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.redis.entity.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductSearchRepository extends JpaRepository<ProductSearch, String> {
    Optional<ProductSearch> findByTypeAndSearchId(SearchType type, Long searchId);
}
