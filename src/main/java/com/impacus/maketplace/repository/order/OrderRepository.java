package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
