package com.impacus.maketplace.dto.qna.request;

import com.impacus.maketplace.entity.qna.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class CreateQuestionDTO {
    @NotNull
    private String orderId; // 주문 ID

    @NotNull
    private Long productOptionId; // 상품 옵션 ID

    @NotBlank
    @Size(max = 500)
    private String contents; // 문의 내용

    public Question toEntity(Long userId, List<String> images) {
        return Question.builder()
                .orderId(orderId)
                .productOptionId(productOptionId)
                .userId(userId)
                .contents(contents)
                .images(images)
                .build();
    }
}
