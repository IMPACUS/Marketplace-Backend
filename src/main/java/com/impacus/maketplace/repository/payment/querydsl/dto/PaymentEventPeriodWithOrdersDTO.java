package com.impacus.maketplace.repository.payment.querydsl.dto;

import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.util.List;

@Data
public class PaymentEventPeriodWithOrdersDTO {
    private final Long paymentEventId;
    private final List<PaymentOrderDTO> paymentOrders;

    @Data
    public static class PaymentOrderDTO {
        private final Long paymentOrderId;
        private final Long quantity;
        private final Long amount;
        private final PaymentOrderStatus status;

        @QueryProjection
        public PaymentOrderDTO(Long paymentOrderId, Long quantity, Long amount, PaymentOrderStatus status) {
            this.paymentOrderId = paymentOrderId;
            this.quantity = quantity;
            this.amount = amount;
            this.status = status;
        }

        public Long getTotalAmount() {
            return quantity * amount;
        }

        public boolean isSuccess() {
            return status == PaymentOrderStatus.SUCCESS;
        }
    }

    @QueryProjection
    public PaymentEventPeriodWithOrdersDTO(Long paymentEventId, List<PaymentOrderDTO> paymentOrders) {
        this.paymentEventId = paymentEventId;
        this.paymentOrders = paymentOrders;
    }

    public Long getTotalAmount() {
        return paymentOrders.stream()
                .filter(PaymentOrderDTO::isSuccess)
                .mapToLong(PaymentOrderDTO::getTotalAmount)
                .sum();
    }
}
