package com.impacus.maketplace.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.enumType.error.PaymentWebhookErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import io.portone.sdk.server.webhook.WebhookVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookVerifyService {

    private final WebhookVerifier portoneWebhook;
    private final ObjectMapper objectMapper;

    public WebhookPaymentDTO paymentVerify(String body, String webhookId, String webhookSignature, String webhookTimestamp) {
        try {
            portoneWebhook.verify(body, webhookId, webhookSignature, webhookTimestamp);
            WebhookPaymentDTO webhookPaymentDTO = objectMapper.readValue(body, WebhookPaymentDTO.class);
            validatePayload(webhookPaymentDTO);
            return webhookPaymentDTO;
        } catch (Exception e) {
            LogUtils.error(this.getClass() + "verify", "Webhook 검증 과정에서 예외 발생", e);
            throw new CustomException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, e);
        }
    }

    private void validatePayload(WebhookPaymentDTO paymentDTO) {
        if (paymentDTO.getData().getPaymentId() == null) {
            throw new CustomException(PaymentWebhookErrorType.REQUIRED_PAYMENT_ID);
        }
        if (paymentDTO.getData().getTransactionId() == null) {
            throw new CustomException(PaymentWebhookErrorType.REQUIRED_TRANSACTION_ID);
        }

        if (paymentDTO.getEventType().equals(WebhookPaymentDTO.WebhookEventType.TRANSACTION_CONFIRM)
                && paymentDTO.getData().getTotalAmount() == null) {
            throw new CustomException(PaymentWebhookErrorType.REQUIRED_TOTAL_AMOUNT);
        }
    }
}
