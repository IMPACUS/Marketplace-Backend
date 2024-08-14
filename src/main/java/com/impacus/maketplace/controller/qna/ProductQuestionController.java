package com.impacus.maketplace.controller.qna;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.qna.AddProductQuestionServiceDTO;
import com.impacus.maketplace.dto.qna.request.AddProductQuestionDTO;
import com.impacus.maketplace.service.qna.ProductQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

/**
 * 상품 문의 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/question")
public class ProductQuestionController {

    private final ProductQuestionService productQuestionService;

    /**
     * 상품 문의 등록
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ApiResponseEntity<Boolean> addProductQuestion(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestPart(name = "body") AddProductQuestionDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        productQuestionService.addProductQuestion(
                AddProductQuestionServiceDTO.builder()
                        .productId(dto.getProductId())
                        .orderId(dto.getOrderId())
                        .userId(user.getId())
                        .contents(dto.getContents())
                        .image(image)
                        .build());
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * 문의 내용 삭제
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @DeleteMapping("/{questionId}")
    public ApiResponseEntity<Boolean> deleteProductQuestion(@PathVariable long questionId,
                                                            @AuthenticationPrincipal CustomUserDetails user) {
        productQuestionService.deleteProductQuestion(questionId, user.getId());
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

}
