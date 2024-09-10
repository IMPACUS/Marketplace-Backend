package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.common.constants.LimitCntConstants;
import com.impacus.maketplace.redis.entity.RecentProductViews;
import com.impacus.maketplace.redis.repository.RecentProductViewsRepository;
import com.impacus.maketplace.redis.repository.mapping.RecentProductViewsMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecentProductViewsService {
    private final RecentProductViewsRepository recentProductViewsRepository;

    /**
     * 최근 본 상품 데이터를 저장하는 함수
     *
     * @param userId
     * @param productId
     */
    @Transactional
    public void addRecentProductView(Long userId, Long productId) {
        RecentProductViews recentProductViews = RecentProductViews.toEntity(userId, productId);
        Optional<RecentProductViews> productViewsOptional = recentProductViewsRepository.findByUserIdAndProductId(userId, productId);

        if (productViewsOptional.isPresent()) {
            recentProductViewsRepository.delete(productViewsOptional.get());
            recentProductViewsRepository.save(recentProductViews);
        } else {
            recentProductViewsRepository.save(recentProductViews);

            checkAndDeleteRecentProductViewCnt(userId);
        }
    }

    /**
     * 사용자에게 저장된 최근 본 상품 데이터 수 확인 후, 삭제하는 함수
     *
     * @param userId
     */
    @Transactional
    public void checkAndDeleteRecentProductViewCnt(Long userId) {
        List<RecentProductViews> recentProductViews = recentProductViewsRepository.findByUserIdOrderByCreateAtAsc(userId);

        if (recentProductViews.size() > LimitCntConstants.PRODUCT_VIEW_LIMIT) {
            recentProductViewsRepository.delete(recentProductViews.get(0));
        }
    }

    public List<Long> findProductIdsByUserId(Long userId, Pageable pageable) {
        Slice<RecentProductViewsMapping> productViewsMappings = recentProductViewsRepository.findByUserIdOrderByCreateAtDesc(userId, pageable);
        return productViewsMappings.stream().map(
                RecentProductViewsMapping::getProductId
        ).toList();
    }
}
