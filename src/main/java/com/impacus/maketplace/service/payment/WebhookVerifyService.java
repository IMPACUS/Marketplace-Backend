package com.impacus.maketplace.service.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentConfirmDTO;
import io.portone.sdk.server.webhook.WebhookVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookVerifyService {

    private final WebhookVerifier portoneWebhook;
    private final ObjectMapper objectMapper;

    public WebhookPaymentConfirmDTO verify(String body, String webhookId, String webhookSignature, String webhookTimestamp) {
        try {
            portoneWebhook.verify(body, webhookId, webhookSignature, webhookTimestamp);
            return objectMapper.readValue(body, WebhookPaymentConfirmDTO.class);
        } catch (Exception e) {
            LogUtils.error(this.getClass() + "verify", "Webhook 검증 과정에서 예외 발생", e);
            throw new CustomException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, e);
        }
    }

}
