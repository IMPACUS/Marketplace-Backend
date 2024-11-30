package com.impacus.maketplace.dto.payment.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPaymentConfirmDTO {
    private WebhookEventType type;
    private String timestamp;
    private PaymentData data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentData {
        private String paymentId;
        private String transactionId;
        private String cancellationId;
    }

    public enum WebhookEventType {
        TRANSACTION_READY("Transaction.Ready"),
        TRANSACTION_PAID("Transaction.Paid"),
        TRANSACTION_VIRTUAL_ACCOUNT_ISSUED("Transaction.VirtualAccountIssued"),
        TRANSACTION_PARTIAL_CANCELLED("Transaction.PartialCancelled"),
        TRANSACTION_CANCELLED("Transaction.Cancelled"),
        TRANSACTION_FAILED("Transaction.Failed"),
        TRANSACTION_PAY_PENDING("Transaction.PayPending"),
        TRANSACTION_CANCEL_PENDING("Transaction.CancelPending"),
        TRANSACTION_CONFIRM("Transaction.Confirm"); // 예시에서 사용된 타입 추가

        private final String value;

        WebhookEventType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static WebhookEventType fromValue(String value) {
            for (WebhookEventType eventType : WebhookEventType.values()) {
                if (eventType.value.equals(value)) {
                    return eventType;
                }
            }
            throw new IllegalArgumentException("Unknown event type: " + value);
        }
    }
}
