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
@RedisHash(value = "emailAuthenticationNumber", timeToLive = 180L)
public class EmailAuthenticationNumber {
    @Id
    private String id;

    @Indexed
    private String email;
    @ColumnDefault("0")
    private String authenticationNumber;

    public EmailAuthenticationNumber(String email, String authenticationNumber) {
        this.email = email;
        this.authenticationNumber = authenticationNumber;
    }
}
