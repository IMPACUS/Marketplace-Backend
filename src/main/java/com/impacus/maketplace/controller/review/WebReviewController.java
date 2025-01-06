package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.request.ReviewReplyDTO;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class WebReviewController {
    private final ReviewService reviewService;
    /**
     * 리뷰 삭제 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/{reviewId}")
    public ApiResponseEntity<Review> deleteReview(
            @PathVariable(name = "reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.CREATED)
                .message("리뷰 답변 수정 성공")
                .build();
    }
}
