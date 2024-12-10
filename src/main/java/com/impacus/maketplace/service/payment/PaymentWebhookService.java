package com.impacus.maketplace.service.payment;

import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final PaymentConfirmService paymentConfirmService;
    private final PaymentSuccessService paymentSuccessService;
    private final PaymentReadyService paymentReadyService;

    public void process(WebhookPaymentDTO payload) {

        // 1. WebhookEventType 확인 후 이벤트 타입에 맞게 설정
        switch (payload.getType()) {
            // 결제 준비
            case TRANSACTION_READY -> paymentReadyService.ready(payload);
            // 결제 승인
            case TRANSACTION_CONFIRM -> paymentConfirmService.confirm(payload);
            // 결제 성공
            case TRANSACTION_PAID -> paymentSuccessService.success(payload);
        }
    }
}
