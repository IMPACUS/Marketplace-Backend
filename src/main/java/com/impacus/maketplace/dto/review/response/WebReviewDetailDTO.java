package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class WebReviewDetailDTO {
    private Long reviewId;
    private Long orderId;
    private String productNumber;
    private String userName;
    private float rating;
    private String contents;
    private String productName;
    private List<String> images;
    private ProductOptionDTO option;
    private String userEmail;
    private LocalDateTime createdAt;
    private WebReviewReplyDTO reply;

    public WebReviewDetailDTO(
            Long reviewId,
            Long orderId,
            String productNumber,
            String userName,
            float rating,
            String contents,
            String productName,
            List<String> images,
            ProductOptionDTO option,
            String userEmail,
            LocalDateTime createdAt,
            WebReviewReplyDTO reply
    ) {
        this.reviewId = reviewId;
        this.orderId = orderId;
        this.productNumber = productNumber;
        this.userName = userName;
        this.rating = rating;
        this.contents = contents;
        this.productName = productName;
        this.images = images;
        this.option = option;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.reply = reply;
    }
}
