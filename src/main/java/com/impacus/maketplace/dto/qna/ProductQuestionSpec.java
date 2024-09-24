package com.impacus.maketplace.dto.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
@Builder
public class ProductQuestionSpec {

    private long sellerId;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean answered;

    private String authorId;

    private String orderNumber;

}
