package com.impacus.maketplace.dto.qna.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AddProductQuestionDTO {

    @NotNull
    private Long productId;

    private Long orderId;

    @NotNull
    @Size(min = 5, max = 500)
    private String contents;

}
