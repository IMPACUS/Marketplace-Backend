package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.response.ProductReviewDTO;
import com.impacus.maketplace.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/product")
public class ProductReviewController {
    private final ReviewService reviewService;

    /**
     * 상품 리뷰 목록 조회 API
     *
     * @param
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/{productId}/reviews")
    public ApiResponseEntity<Page<ProductReviewDTO>> findReviewsByProductId(
            @PathVariable(value = "productId") Long productId,
            @PageableDefault(size = 2, direction = Sort.Direction.DESC, sort = "createAt") Pageable pageable
    ) {
        Page<ProductReviewDTO> result = reviewService.findReviewsByProductId(productId, pageable);
        return ApiResponseEntity
                .<Page<ProductReviewDTO>>builder()
                .code(HttpStatus.OK)
                .message("상품 리뷰 목록 조회 성공")
                .data(result)
                .build();
    }
}
