package com.impacus.maketplace.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@TestConfiguration
public class RedisTestConfig {

    /**
     * 테스트 환경에서 사용할 RedisConnectionFactory 빈.
     * 실제 Redis 서버가 필요하지 않는 상황에서 해당 의존성 사용
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }
}
