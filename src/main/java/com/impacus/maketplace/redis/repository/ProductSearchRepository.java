package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.common.enumType.SearchType;
import com.impacus.maketplace.redis.entity.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductSearchRepository extends JpaRepository<ProductSearch, String> {
}
