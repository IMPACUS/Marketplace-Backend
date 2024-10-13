package com.impacus.maketplace.repository.payment;

import com.impacus.maketplace.entity.payment.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {
}
