package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.request.CreateReviewDTO;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class AppReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 등록 API
     *
     * @param images 이미지 파일
     * @param dto         리뷰 폼
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ApiResponseEntity<Review> addReview(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("images") List<MultipartFile> images,
            @Valid @RequestPart("review") CreateReviewDTO dto
    ) {
        reviewService.addReview(user.getId(), images, dto);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.OK)
                .message("리뷰 생성 성공")
                .build();
    }

    /**
     * 리뷰 내용 수정 API
     *
     * @param dto 리뷰 폼
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("/{reviewId}")
    public ApiResponseEntity<Review> updateReview(
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ReviewDTO dto
    ) {
        reviewService.updateReview(reviewId, dto);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.OK)
                .message("리뷰 내용 수정 성공")
                .build();
    }

    /**
     * 리뷰 이미지 수정 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PutMapping("/{reviewId}/images")
    public ApiResponseEntity<Review> updateReviewImages(
            @PathVariable(name = "reviewId") Long reviewId,
            @RequestBody List<String> images
    ) {
        reviewService.updateReviewImages(reviewId, images);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.OK)
                .message("리뷰 내용 이미지 성공")
                .build();
    }
}
