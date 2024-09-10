package com.impacus.maketplace.repository.product.history;

import com.impacus.maketplace.entity.product.history.ProductOptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionHistoryRepository extends JpaRepository<ProductOptionHistory, Long> {
}
