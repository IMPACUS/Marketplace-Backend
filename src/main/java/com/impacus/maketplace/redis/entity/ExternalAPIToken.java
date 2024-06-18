package com.impacus.maketplace.redis.entity;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.APIProvider;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Getter
@NoArgsConstructor
@RedisHash(value = "externalAPIToken")
public class ExternalAPIToken extends BaseEntity {
    @Id
    private String id;

    private String accessToken;

    private String refreshToken;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private Long ttl;

    @Indexed
    @Enumerated(EnumType.STRING)
    private APIProvider apiProvider;
}
