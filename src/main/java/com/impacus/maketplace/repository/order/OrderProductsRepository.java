package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.entity.order.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Long> {
}
