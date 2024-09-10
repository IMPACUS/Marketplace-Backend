package com.impacus.maketplace.repository.review;

import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;
import com.impacus.maketplace.dto.review.ReviewSellerDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ReviewCustomRepository {
    // 주문 번호의 따른 구매자용 리뷰 조회
    List<ReviewBuyerDTO> displayViewBuyerReview(Long userId);

    // 리뷰 삭제

    //
    ReviewBuyerDTO displayViewBuyerReviewOne(Long userId, Long orderId);

    // 판매자용 리스트 조회
    Slice<ReviewSellerDTO> displaySellerReviewList(Pageable pageable, Long userId, String search);

    // 판매자용 리뷰 상세 조회 (댓글 달기 포함)
    ReviewSellerDTO displaySellerReviewDetail(Long userId, Long reviewId, String sellerComment);

}
