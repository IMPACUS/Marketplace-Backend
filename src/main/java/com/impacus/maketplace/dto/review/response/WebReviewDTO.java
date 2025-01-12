package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.common.annotation.excel.ExcelColumn;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.service.review.ReviewService;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class WebReviewDTO {
    private Long reviewId;

    @ExcelColumn(headerName = "별점")
    private float rating;

    @ExcelColumn(headerName = "리뷰 내용")
    private String contents;

    @ExcelColumn(headerName = "주문자")
    private String userName;

    @ExcelColumn(headerName = "아이디")
    private String userEmail;

    @ExcelColumn(headerName = "작성일자")
    private LocalDateTime createdAt;
    private boolean isDeleted;

    @ExcelColumn(headerName = "포인트 지급")
    private long allocatedPoint;

    public WebReviewDTO(
            Long reviewId,
            float rating,
            String contents,
            String userName,
            String userEmail,
            LocalDateTime createdAt,
            boolean isDeleted,
            List<String> images
    ) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.userName = userName;
        this.userEmail = StringUtils.getEmailInfo(userEmail).getEmail();
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;

        if (images == null || images.isEmpty()) {
            this.allocatedPoint = ReviewService.TEXT_REVIEW_POINT;
        } else {
            this.allocatedPoint = ReviewService.PHOTO_REVIEW_POINT;
        }
    }
}
