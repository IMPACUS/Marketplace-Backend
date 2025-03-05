package com.impacus.maketplace.dto.review.request;

import jakarta.validation.constraints.*;
import lombok.Getter;


@Getter
public class ReviewDTO {
    @NotNull
    @Min(0)
    @Max(5)
    private Float rating; // 별점

    @NotBlank
    @Size(max = 500)
    private String contents; // 리뷰 내용

}
