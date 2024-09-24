package com.impacus.maketplace.controller.qna;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.qna.AddProductQuestionServiceDTO;
import com.impacus.maketplace.dto.qna.request.AddProductQuestionDTO;
import com.impacus.maketplace.dto.qna.request.GetProductsParams;
import com.impacus.maketplace.dto.qna.response.SellerProductQuestionResponseDTO;
import com.impacus.maketplace.service.qna.ProductQuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @PreAuthorize("hasRole('CERTIFIED_USER')")
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
    @PreAuthorize("hasAnyRole('CERTIFIED_USER', 'ADMIN', 'OWNER', 'PRINCIPAL_ADMIN')")
    @DeleteMapping("/{questionId}")
    public ApiResponseEntity<Boolean> deleteProductQuestion(@PathVariable long questionId) {
        productQuestionService.deleteProductQuestionById(questionId);
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('APPROVED_SELLER')")
    @GetMapping
    public ApiResponseEntity<Page<SellerProductQuestionResponseDTO>> getQuestions(
            @AuthenticationPrincipal CustomUserDetails user, @Valid GetProductsParams params, Pageable pageable) {
        if (params.getStartDate() != null
                && params.getEndDate() != null
                && params.getEndDate().isBefore(params.getStartDate())) {
            throw new CustomException(CommonErrorType.INVALID_END_DATE);
        }
        return ApiResponseEntity.of(productQuestionService.getProducts(user.getId(), params, pageable));
    }

}
