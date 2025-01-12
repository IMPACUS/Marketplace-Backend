package com.impacus.maketplace.dto.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Builder
@Getter
public class AddProductQuestionServiceDTO {
    private long productId;
    private Long orderId;
    private long userId;
    private String contents;
    private MultipartFile image;
}
