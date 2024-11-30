package com.impacus.maketplace.controller.payment;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentConfirmDTO;
import com.impacus.maketplace.service.payment.WebhookVerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class WebhookPaymentController {

    private final WebhookVerifyService webhookVerifyService;

    @PostMapping("/webhook/payment-confirm")
    public ApiResponseEntity<Void> paymnetConfirm(@RequestBody String body,
                                                  @RequestHeader("webhook-id") String webhookId,
                                                  @RequestHeader("webhook-timestamp") String webhookTimestamp,
                                                  @RequestHeader("webhook-signature") String webhookSignature) {

        WebhookPaymentConfirmDTO payload = webhookVerifyService.verify(body, webhookId, webhookSignature, webhookTimestamp);

        return ApiResponseEntity
                .<Void>builder()
                .build();

    }
}
