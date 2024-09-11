package com.impacus.maketplace.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "key.payment")
public class PaymentConfig {
    private String storeId;
    private String smartroChannelKey;
    private String smartroKey;
    private String smartroCancelPassword;
    private String smartroApiKey;
    private String kakaoPayChannelKey;
}
