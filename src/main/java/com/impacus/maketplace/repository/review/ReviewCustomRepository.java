package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;

import java.util.List;

public interface ReviewCustomRepository {
    // 주문 번호의 따른 구매자용 리뷰 조회
    List<ReviewBuyerDTO> displayViewBuyerReview(Long userId);

    // 리뷰 삭제

    //
    ReviewBuyerDTO displayViewBuyerReviewOne(Long userId, Long orderId);
}
