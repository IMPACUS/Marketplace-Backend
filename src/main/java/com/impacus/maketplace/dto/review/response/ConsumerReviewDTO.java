package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


@Getter
@NoArgsConstructor
public class ConsumerReviewDTO {
    private Long reviewId; // 리뷰 인덱스 번호
    private Long orderId; // 주문 인덱스 번호
    private String contents; //  리뷰 내용
    private String replyContents; // 판매자 답글
    private List<String> images; // 리뷰 이미지
    private ProductOptionDTO option;
    private LocalDateTime createdAt;
    private long quantity;
    private long amount;
}
