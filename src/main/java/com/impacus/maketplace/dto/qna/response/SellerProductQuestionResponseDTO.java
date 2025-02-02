package com.impacus.maketplace.dto.qna.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.impacus.maketplace.entity.qna.Question;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class SellerProductQuestionResponseDTO {

    private long questionId;

    private String contents;

    private Long orderId;

    private String customerName;

    private String customerId;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime createdTime;


    public static SellerProductQuestionResponseDTO fromEntity(Question entity) {
        return SellerProductQuestionResponseDTO.builder()
                .questionId(entity.getId())
                .contents(entity.getContents())
                .orderId(entity.getPaymentEventId())
                .customerName(entity.getUserName())
                .build();
    }

}
