package com.impacus.maketplace.controller.payment;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.payment.request.WebhookPaymentDTO;
import com.impacus.maketplace.service.payment.webhook.WebhookHandlerService;
import com.impacus.maketplace.service.payment.webhook.utils.WebhookVerifyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class WebhookPaymentController {

    private final WebhookVerifyService webhookVerifyService;
    private final WebhookHandlerService paymentWebhookService;

    @PostMapping("/webhook")
    public ApiResponseEntity<Boolean> paymentWebhook(@RequestBody String body,
                                                     @RequestHeader("webhook-id") String webhookId,
                                                     @RequestHeader("webhook-timestamp") String webhookTimestamp,
                                                     @RequestHeader("webhook-signature") String webhookSignature) {

        WebhookPaymentDTO payload = webhookVerifyService.paymentVerify(body, webhookId, webhookSignature, webhookTimestamp);

        paymentWebhookService.process(payload);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }
}
