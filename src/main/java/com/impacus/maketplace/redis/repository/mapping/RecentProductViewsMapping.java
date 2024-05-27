package com.impacus.maketplace.redis.repository.mapping;

import java.time.LocalDateTime;

public interface RecentProductViewsMapping {
    Long getProductId();

    LocalDateTime getCreateAt();
}
