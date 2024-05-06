package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "loginFailAttempt", timeToLive = 86400L)
public class LoginFailAttempt {

    @Id
    private String id;

    @Indexed
    private String email;
    @ColumnDefault("0")
    private int failAttemptCnt;

    public void increaseFailAttemptCnt() {
        this.failAttemptCnt += 1;
    }

    public void resetFailAttemptCnt() {
        this.failAttemptCnt = 0;
    }

    public LoginFailAttempt(String email) {
        this.email = email;
    }
}
