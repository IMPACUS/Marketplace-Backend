package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "certificationRequestNumber", timeToLive = 86400L)
public class CertificationRequestNumber {
    @Id
    private String id;

    @Indexed
    private String reqNumber;

    public CertificationRequestNumber(String reqNumber) {
        this.reqNumber = reqNumber;
    }

    public static CertificationRequestNumber toEntity(String reqNumber) {
        return new CertificationRequestNumber(reqNumber);
    }
}
