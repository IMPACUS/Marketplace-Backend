package com.impacus.maketplace.repository.payment;

import com.impacus.maketplace.entity.payment.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {

    boolean existsByOrderId(String orderId);
}
