package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.response.WebReviewDTO;
import com.impacus.maketplace.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    /**
     * 리뷰 조회
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PostMapping
    public ApiResponseEntity<Page<WebReviewDTO>> findReviews(
            @PageableDefault(size = 5, direction = Sort.Direction.DESC, sort = "createAt") Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "start-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(value = "end-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt
    ) {
        Page<WebReviewDTO> result = reviewService.findReviews(
                pageable, keyword, startAt, endAt
        );

        return ApiResponseEntity
                .<Page<WebReviewDTO>>builder()
                .code(HttpStatus.OK)
                .message("리뷰 조회 성공")
                .data(result)
                .build();
    }

    /**
     * 리뷰 단건 조회
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PostMapping("/{reviewId}")
    public ApiResponseEntity<WebReviewDetailDTO> findReview(
            @PathVariable(name = "reviewId") Long reviewId
    ) {
        WebReviewDetailDTO result = reviewService.findReview(
                reviewId
        );

        return ApiResponseEntity
                .<WebReviewDetailDTO>builder()
                .code(HttpStatus.OK)
                .message("리뷰 단건 조회 성공")
                .data(result)
                .build();
    }
}
