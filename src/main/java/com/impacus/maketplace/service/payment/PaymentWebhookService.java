package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentConfirmService paymentConfirmService;

    public void process(WebhookPaymentDTO payload) {

        // 1. WebhookEventType 확인 후 이벤트 타입에 맞게 설정
        if (payload.getEventType().equals(WebhookPaymentDTO.WebhookEventType.TRANSACTION_CONFIRM)) {
            // 결제 승인
            paymentConfirmService.confirmService(payload);
        }
    }
}
