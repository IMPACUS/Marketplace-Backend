package com.impacus.maketplace.config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableFeignClients(basePackages = {

})
public class OpenFeignConfig {

    @Bean
    public Retryer feignRetryer() {
        // 1000ms 후 처음 재시도를 하고, 이후 10초 간격으로 최대 10번까지 재시도하도록 설정
        return new Retryer.Default(1000, TimeUnit.SECONDS.toMillis(10), 10);
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.HEADERS;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate
                -> requestTemplate
                .header("Content-Type", "application/json");
    }
}
