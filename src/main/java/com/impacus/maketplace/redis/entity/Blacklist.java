package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "blacklist", timeToLive = 300L) // 3분
public class Blacklist {
    @Id
    private String id;

    @Indexed
    private String accessToken;
}
