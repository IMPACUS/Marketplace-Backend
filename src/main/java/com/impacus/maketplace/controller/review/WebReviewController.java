package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.service.review.ReviewService;
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
    @DeleteMapping("/{reviewId}")
    public ApiResponseEntity<Void> deleteReview(
            @PathVariable(name = "reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        return ApiResponseEntity
                .<Void>builder()
                .code(HttpStatus.OK)
                .message("리뷰 삭제 성공")
                .build();
    }

    /**
     * 리뷰 복구 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PatchMapping("/{reviewId}/restore")
    public ApiResponseEntity<Void> restoreReview(
            @PathVariable(name = "reviewId") Long reviewId) {
        reviewService.restoreReview(reviewId);

        return ApiResponseEntity
                .<Void>builder()
                .code(HttpStatus.OK)
                .message("리뷰 복구 성공")
                .build();
    }
}
