package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor
@RedisHash(value = "verificationCode", timeToLive = 180L) // 3분
public class VerificationCode {
    @Id
    private String id;

    @Indexed
    private String email; //identifier
    
    @Indexed
    private String code;

    public VerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static VerificationCode toEntity(String email, String code) {
        return new VerificationCode(email, code);
    }
}
