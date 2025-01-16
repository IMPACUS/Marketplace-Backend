package com.impacus.maketplace.dto.review.request;

import com.impacus.maketplace.entity.review.ReviewReply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ReviewReplyDTO {
    @NotBlank
    @Size(max = 500)
    private String contents;

    public ReviewReply toEntity(Long reviewId) {
        return new ReviewReply(reviewId, this.contents);
    }
}
