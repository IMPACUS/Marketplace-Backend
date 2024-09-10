package com.impacus.maketplace.repository.product.history;

import com.impacus.maketplace.entity.product.history.ProductHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistory, Long> {
}
