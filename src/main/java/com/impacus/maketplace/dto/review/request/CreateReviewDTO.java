package com.impacus.maketplace.dto.review.request;

import com.impacus.maketplace.entity.review.Review;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.Map;

@Getter
public class CreateReviewDTO extends ReviewDTO {
    @NotNull
    private Long orderId; // 주문 ID

    @NotNull
    private Long productOptionId; // 상품 옵션 ID

    public Review toEntity(Long userId, Map<Long, String> reviewImages) {
        return new Review(
                this.orderId,
                this.productOptionId,
                userId,
                super.getContents(),
                reviewImages,
                super.getRating()
        );
    }
}
