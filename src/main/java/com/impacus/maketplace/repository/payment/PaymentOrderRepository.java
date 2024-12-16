package com.impacus.maketplace.repository.payment;

import com.impacus.maketplace.entity.payment.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

    Optional<List<PaymentOrder>> findByPaymentEventId(Long paymentEventId);
}
