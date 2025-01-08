package com.impacus.maketplace.dto.review.response;

import com.impacus.maketplace.common.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class WebReviewDTO {
    private Long reviewId;
    private float rating;
    private String contents;
    private String userName;
    private String userEmail;
    private LocalDateTime createdAt;
    private boolean isDeleted;

    public WebReviewDTO(
            Long reviewId,
            float rating,
            String contents,
            String userName,
            String userEmail,
            LocalDateTime createdAt,
            boolean isDeleted
    ) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.contents = contents;
        this.userName = userName;
        this.userEmail = StringUtils.getEmailInfo(userEmail).getEmail();
        this.createdAt = createdAt;
        this.isDeleted = isDeleted;
    }
}
