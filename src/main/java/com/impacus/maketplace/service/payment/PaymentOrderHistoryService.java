package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.entity.payment.PaymentOrderHistory;
import com.impacus.maketplace.repository.payment.PaymentOrderHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentOrderHistoryService {

    private final PaymentOrderHistoryRepository paymentOrderHistoryRepository;

    @Transactional
    public void update(PaymentOrder paymentOrder, PaymentOrderStatus newStatus, String reason) {
        List<PaymentOrder> paymentOrderIds = Collections.singletonList(paymentOrder);
        updateAll(paymentOrderIds, newStatus, reason);
    }

    @Transactional
    public void updateAll(List<PaymentOrder> paymentOrders, PaymentOrderStatus newStatus, String reason) {

        List<PaymentOrderHistory> paymentOrderHistories = paymentOrders.stream().map(paymentOrder ->
                        PaymentOrderHistory.builder()
                                .paymentOrderId(paymentOrder.getId())
                                .previousStatus(paymentOrder.getStatus())
                                .newStatus(newStatus)
                                .reason(reason)
                                .build()
                )
                .toList();

        paymentOrderHistoryRepository.saveAll(paymentOrderHistories);
    }
}
