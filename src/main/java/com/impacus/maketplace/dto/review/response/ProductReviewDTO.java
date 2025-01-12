package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductReviewDTO {
    private Long reviewId;
    private Long orderId;
    private float rating;
    private String contents;
    private List<String> images;
    private ProductOptionDTO option;
    private String userEmail;
    private LocalDateTime createdAt;

    public ProductReviewDTO(
            Long reviewId,
            Long orderId,
            float rating,
            String contents,
            List<String> images,
            ProductOptionDTO option,
            String userEmail,
            LocalDateTime createdAt
    ) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.rating = rating;
        this.contents = contents;
        this.images = images;
        this.option = option;
        this.userEmail = StringUtils.getEmailInfo(userEmail).getEmail();
        this.createdAt = createdAt;
    }
}
