package com.impacus.maketplace.dto.review;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.entity.review.Review;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;


@Getter
public class ReviewDTO {
    @NotNull
    private Long orderId; // 주문 ID

    @NotNull
    private Long productOptionId; // 상품 옵션 ID

    @NotNull
    @Min(0)
    @Max(5)
    private Float rating; // 별점

    @NotBlank
    @Size(max = 500)
    private String contents; // 리뷰 내용

    public Review toEntity(Long userId, Map<Long, String> reviewImages) {
        return new Review(
          orderId,
          productOptionId,
          userId,
          contents,
          reviewImages,
          rating
        );
    }
}
