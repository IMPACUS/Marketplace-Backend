package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.request.ReviewDTO;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
            @Valid @RequestPart("images") List<MultipartFile> images,
            @RequestPart("review") ReviewDTO dto
    ) {
        reviewService.addReview(user.getId(), images, dto);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.OK)
                .message("리뷰 생성 성공")
                .build();
    }
}
