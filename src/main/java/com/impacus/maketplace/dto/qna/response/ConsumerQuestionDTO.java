package com.impacus.maketplace.dto.qna.response;

import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ConsumerQuestionDTO {
    private Long questionId;
    private Long orderId; // 주문 인덱스 번호
    private String contents; //   내용
    private String replyContents; // 판매자 답글
    private List<String> images; //  이미지
    private ProductOptionDTO option;
    private LocalDateTime createdAt;

    public ConsumerQuestionDTO(
            Long questionId,
            Long orderId,
            String contents,
            String replyContents,
            List<String> images,
            ProductOptionDTO option,
            LocalDateTime createdAt
    ) {
        this.questionId = questionId;
        this.orderId = orderId;
        this.contents = contents;
        this.replyContents = replyContents;
        this.images = images;
        this.option = option;
        this.createdAt = createdAt;
    }
}
