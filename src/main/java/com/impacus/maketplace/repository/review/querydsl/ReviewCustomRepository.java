package com.impacus.maketplace.repository.review.querydsl;

import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.dto.review.response.ConsumerReviewDTO;
import com.impacus.maketplace.dto.review.response.ProductReviewDTO;
import com.impacus.maketplace.dto.review.response.WebReviewDTO;
import com.impacus.maketplace.dto.review.response.WebReviewDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.List;

public interface ReviewCustomRepository {


    void deleteReview(Long reviewId);

    void restoreReview(Long reviewId);

    Page<ProductReviewDTO> findReviewsByProductId(Long productId, Pageable pageable);

    void updateReview(Long reviewId, ReviewDTO dto);

    void updateReviewImages(Long reviewId, List<String> images);

    Slice<ConsumerReviewDTO> findUserReviews(Long userId, Pageable pageable);

    Page<WebReviewDTO> findReviews(Pageable pageable, String keyword, LocalDate startAt, LocalDate endAt);

    WebReviewDetailDTO findReview(Long reviewId);

    List<WebReviewDTO> findReviewsByIds(IdsDTO dto);
}
