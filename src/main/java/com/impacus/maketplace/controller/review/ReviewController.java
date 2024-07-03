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
     * 구매자 리스트
     * @return
     */
    //    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("buyers-review")
    public ApiResponseEntity<?> displayBuyersReviewList(
            @RequestParam("userId") Long userId,
            @RequestParam("orderId") Long orderId
    ) {
        List<ReviewBuyerDTO> reviewBuyerDTOS = reviewService.displayBuyersReviewList(userId, orderId);
        return ApiResponseEntity
                .builder()
                .code(HttpStatus.OK)
                .message("리뷰 리스트 조회 성공")
                .data(reviewBuyerDTOS)
                .build();
    }

    /**
     * 리뷰 등록
     * @param productImage
     * @param form
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
}

