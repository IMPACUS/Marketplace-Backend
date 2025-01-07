package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
            Map<String, String> images,
            ProductOptionDTO option,
            String userEmail,
            LocalDateTime createdAt
    ) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.rating = rating;
        this.contents = contents;
        this.images = images.values().stream().toList();
        this.option = option;
        this.userEmail = StringUtils.getEmailInfo(userEmail).getEmail();
        this.createdAt = createdAt;
    }
}
