package com.impacus.maketplace.repository.order;

import com.impacus.maketplace.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepositroy extends JpaRepository<Order, Long> {
}
