package com.impacus.maketplace.redis.entity;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "emailVerificationCode", timeToLive = 180L)
public class EmailVerificationCode {
    @Id
    private String id;

    @Indexed
    private String email;
    @ColumnDefault("0")
    private String code;

    public EmailVerificationCode(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public static EmailVerificationCode toEntity(String email, String code) {
        return new EmailVerificationCode(email, code);
    }
}
