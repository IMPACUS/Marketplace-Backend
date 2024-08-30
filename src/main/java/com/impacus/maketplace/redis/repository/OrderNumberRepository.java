package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.OrderNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNumberRepository extends JpaRepository<OrderNumber, Long> {
    boolean existsByOrderNumber(String orderNumber);
}
