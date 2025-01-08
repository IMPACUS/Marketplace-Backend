package com.impacus.maketplace.service.payment.webhook;

import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import com.impacus.maketplace.service.payment.webhook.process.PaymentCancelService;
import com.impacus.maketplace.service.payment.webhook.process.PaymentConfirmService;
import com.impacus.maketplace.service.payment.webhook.process.PaymentPreparationService;
import com.impacus.maketplace.service.payment.webhook.process.PaymentCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookHandlerService {

    private final PaymentConfirmService paymentConfirmService;
    private final PaymentCompletionService paymentSuccessService;
    private final PaymentPreparationService paymentReadyService;
    private final PaymentCancelService paymentCancelService;

    public void process(WebhookPaymentDTO payload) {

        // 1. WebhookEventType 확인 후 이벤트 타입에 맞게 설정
        switch (payload.getType()) {
            // 결제 준비
            case TRANSACTION_READY -> paymentReadyService.ready(payload);
            // 결제 승인
            case TRANSACTION_CONFIRM -> paymentConfirmService.confirm(payload);
            // 결제 성공
            case TRANSACTION_PAID -> paymentSuccessService.success(payload);
            // 결제 실패
            case TRANSACTION_FAILED -> paymentCancelService.fail(payload);
        }
    }
}
