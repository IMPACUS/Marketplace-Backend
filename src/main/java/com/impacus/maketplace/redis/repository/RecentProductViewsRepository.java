package com.impacus.maketplace.redis.repository;

import com.impacus.maketplace.redis.entity.RecentProductViews;
import com.impacus.maketplace.redis.repository.mapping.RecentProductViewsMapping;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecentProductViewsRepository extends JpaRepository<RecentProductViews, String> {

    Optional<RecentProductViews> findByUserIdAndProductId(Long userId, Long productId);

    List<RecentProductViews> findByUserIdOrderByCreateAtAsc(Long userId);

    Slice<RecentProductViewsMapping> findByUserIdOrderByCreateAtDesc(Long userId, Pageable pageable);
}
