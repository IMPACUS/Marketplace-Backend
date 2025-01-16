package com.impacus.maketplace.config;

import com.impacus.maketplace.common.enumType.error.PaymentErrorType;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import com.impacus.maketplace.common.exception.CustomException;
import io.portone.sdk.server.webhook.WebhookVerifier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "key.payment")
public class PaymentConfig {

    private String storeId;
    private Smartro smartro;
    private KakaoPay kakaoPay;
    private String webhookKey;

    public String getChannelKeyByPaymentMethod(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case CARD:
                return smartro.getChannelKey();
            case KAKAO_PAY:
                return kakaoPay.getChannelKey();
            default:
                throw new CustomException(PaymentErrorType.NOT_FOUND_CHANNEL_KEY);
        }
    }

    @Getter
    @Setter
    public static class Smartro {
        private String channelKey;
        private String key;
        private String cancelPassword;
        private String apiKey;
    }

    @Getter
    @Setter
    public static class KakaoPay {
        private String channelKey;
    }

    @Bean
    public WebhookVerifier webhookVerifier() {
        return new WebhookVerifier(this.webhookKey);
    }
}
