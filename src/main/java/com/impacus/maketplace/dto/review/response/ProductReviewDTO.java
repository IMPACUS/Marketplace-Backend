package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductReviewDTO {
    private Long reviewId;
    private Long orderId;
    private float ration;
    private String contents;
    private List<String> images;
    private ProductOptionDTO option;
    private String userEmail;
    private LocalDateTime createdAt;
}
