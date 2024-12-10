package com.impacus.maketplace.repository.payment;

import com.impacus.maketplace.entity.payment.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentEventRepository extends JpaRepository<PaymentEvent, Long> {

    boolean existsByPaymentId(String paymentId);

    Optional<Long> findIdByPaymentId(String paymentId);

    Optional<PaymentEvent> findByPaymentId(String paymentId);
}
