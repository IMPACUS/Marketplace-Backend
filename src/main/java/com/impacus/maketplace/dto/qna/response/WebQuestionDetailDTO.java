package com.impacus.maketplace.dto.qna.response;

import com.impacus.maketplace.dto.product.response.ProductOptionDTO;
import com.impacus.maketplace.dto.review.response.WebReplyDTO;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class WebQuestionDetailDTO {
    private Long questionId;
    private String orderId;
    private String productNumber;
    private String contents;
    private String productName;
    private List<String> images;
    private List<String> productImages;
    private ProductOptionDTO option;
    private String userEmail;
    private String userName;
    private LocalDateTime createdAt;
    private WebReplyDTO reply;
}
