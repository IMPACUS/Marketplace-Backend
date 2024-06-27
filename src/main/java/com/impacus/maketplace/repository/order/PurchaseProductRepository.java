package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.entity.order.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseProductRepository extends JpaRepository<PurchaseProduct, Long> {
}
