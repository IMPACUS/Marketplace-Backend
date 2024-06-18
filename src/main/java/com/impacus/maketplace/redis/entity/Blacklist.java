package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor
@RedisHash(value = "blacklist")
public class Blacklist {
    @Id
    private String id;

    @Indexed
    private String accessToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long expiration;

    public Blacklist(String accessToken, Long expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }
}
