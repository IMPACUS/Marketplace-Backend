package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "orderNumber", timeToLive = 30 * 60L) // 30분
public class OrderNumber {
    @Id
    private String id;

    private String orderNumber;

    public OrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
