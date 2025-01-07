package com.impacus.maketplace.repository.review.querydsl;

import com.impacus.maketplace.dto.review.response.ProductReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewCustomRepository {


    void deleteReview(Long reviewId);

    void restoreReview(Long reviewId);

    Page<ProductReviewDTO> findReviewsByProductId(Long productId, Pageable pageable);
}
