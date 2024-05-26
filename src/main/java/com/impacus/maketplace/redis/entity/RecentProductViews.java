package com.impacus.maketplace.redis.entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@RedisHash(value = "recentProductViews", timeToLive = 1814400L) // 3주
public class RecentProductViews {
    @Id
    private String id;
    @Indexed
    private Long userId;
    @Indexed
    private Long productId;

    private LocalDateTime createAt;

    public RecentProductViews(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;
        this.createAt = LocalDateTime.now();
    }

    public static RecentProductViews toEntity(Long userId, Long productId) {
        return new RecentProductViews(userId, productId);
    }
}
