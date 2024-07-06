package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.ReviewBuyerDTO;
import com.impacus.maketplace.dto.review.ReviewDTO;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.service.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * (1) 구매자 관점 조회 리스트
     * @param userId 구매자 번호
     * @return
     */
    //    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("buyers-review-list")
    public ApiResponseEntity<?> displayBuyersReviewList(
            @RequestParam(value = "userId") Long userId
    ) {
        List<ReviewBuyerDTO> reviewBuyerDTOS = reviewService.displayBuyersReviewList(userId);
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("리뷰 리스트 조회 성공")
                .data(reviewBuyerDTOS)
                .build();
    }

    /**
     * (2) 구매자 관점 - 리뷰 등록
     *
     * @param productImage 이미지 파일
     * @param form 리뷰 폼
     * @return
     */
    @PostMapping("buyer-review")
    public ApiResponseEntity<Review> doWriteReview(
            @RequestPart("product-image") @Valid MultipartFile productImage,
            @RequestPart("form") ReviewDTO form
    ) {
        Review review = reviewService.doWriteReview(productImage, form);
        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.OK)
                .message("리뷰 리스트 조회 성공")
                .data(review)
                .build();
    }

    /**
     * (3) 구매자 관점 상세 리뷰
     * @param userId 구매자 번호
     * @param orderId 주문 인덱스 번호 (생략 가능) - 판매자
     * @return
     */
    //    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("buyers-review")
    public ApiResponseEntity<?> displayBuyersReviewOne(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "orderId") Long orderId
    ) {
        ReviewBuyerDTO reviewBuyerDTO = reviewService.displayBuyersReviewOne(userId, orderId);
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("리뷰 리스트 조회 성공")
                .data(reviewBuyerDTO)
                .build();
    }
}

