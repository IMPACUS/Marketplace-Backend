package com.impacus.maketplace.service.payment.webhook.process;

import com.impacus.maketplace.common.enumType.error.PaymentWebhookErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentOrderStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import com.impacus.maketplace.entity.payment.PaymentEvent;
import com.impacus.maketplace.entity.payment.PaymentOrder;
import com.impacus.maketplace.repository.payment.PaymentEventRepository;
import com.impacus.maketplace.repository.payment.PaymentOrderRepository;
import com.impacus.maketplace.service.payment.PaymentOrderHistoryService;
import com.impacus.maketplace.service.payment.utils.PaymentStatusValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO.WebhookEventType.TRANSACTION_READY;

@Service
@RequiredArgsConstructor
public class PaymentPreparationService {

    private final PaymentOrderRepository paymentOrderRepository;
    private final PaymentEventRepository paymentEventRepository;
    private final PaymentOrderHistoryService paymentOrderHistoryService;
    private final PaymentStatusValidationService paymentValidationService;

    @Transactional
    public void ready(WebhookPaymentDTO webhookPaymentDTO) {

        // 1. PaymentId를 통해서 PaymentEvent 조회
        String paymentId = webhookPaymentDTO.getData().getPaymentId();
        PaymentEvent paymentEvent = paymentEventRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new CustomException(PaymentWebhookErrorType.NOT_FOUND_PAYMENT_EVENT_BY_PAYMENT_ID));

        // 2. PaymentEventId를 통해서 PaymentOrder 전부 조회
        List<PaymentOrder> paymentOrders = paymentOrderRepository.findByPaymentEventId(paymentEvent.getId())
                .orElseThrow(() -> new CustomException(PaymentWebhookErrorType.NOT_FOUND_PAYMENT_ORDER_BY_PAYMENT_EVENT_ID));

        // 3. 올바르지 않은 상태라면 취소
        paymentValidationService.validatePaymentStatus(TRANSACTION_READY, paymentOrders);

        // 4. 상태 전부 변경
        paymentOrderHistoryService.updateAll(paymentOrders, PaymentOrderStatus.READY, "payment ready");

        paymentOrders.forEach(paymentOrder ->
                paymentOrder.changeStatus(PaymentOrderStatus.READY));
    }
}
