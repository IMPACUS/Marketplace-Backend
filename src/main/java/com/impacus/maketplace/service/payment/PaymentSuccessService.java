package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.common.enumType.error.PaymentWebhookErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.service.payment.postprocess.LedgerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PaymentSuccessService {

    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderRepository paymentOrderRepository;
    private final LedgerService ledgerService;

    /**
     * 결제 성공 처리
     * 상황 부연 설명: 결제 승인 과정을 성공적으로 처리한 뒤 로직
     */
    @Transactional
    public void success(WebhookPaymentDTO payload) {

        // 1. Payment Event 조회
        String paymentId = payload.getData().getPaymentId();
        PaymentEvent paymentEvent = paymentEventRepository.findByPaymentKey(paymentId)
                .orElseThrow(() -> new CustomException(PaymentWebhookErrorType.NOT_FOUND_PAYMENT_EVENT_BY_PAYMENT_ID));

        // 2. Payment Order 조회
        List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId())
                .orElseThrow(() -> new CustomException(PaymentWebhookErrorType.NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID));

        // 3. Payment Order 결제 주문 상태 변경
        paymentOrders.forEach(paymentOrder -> paymentOrder.changeStatus(PaymentOrderStatus.SUCCESS));

        // 4. 모든 Payment Order 원장/(정산) 처리 및 상태 업데이트


        // 5. 모든 Payment Order 결제 완료 상태 변경

        // 6. Payment Event 결제 완료 상태 변경
    }
}
