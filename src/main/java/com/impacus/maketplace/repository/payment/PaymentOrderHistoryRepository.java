package com.impacus.maketplace.repository.payment;

import com.impacus.maketplace.entity.payment.PaymentOrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOrderHistoryRepository extends JpaRepository<PaymentOrderHistory, Long> {
}
