package com.impacus.maketplace.controller.qna;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.qna.request.CreateQuestionDTO;
import com.impacus.maketplace.service.qna.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.util.List;

/**
 *  문의 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/question")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * 문의 등록
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping
    public ApiResponseEntity<Boolean> addQuestion(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("images") List<MultipartFile> images,
            @Valid @RequestPart("question") CreateQuestionDTO dto
    ) {
        questionService.addQuestion(
                user.getId(),
                images,
                dto
        );
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * 문의 내용 삭제
     */
    @PreAuthorize("hasAnyRole('CERTIFIED_USER', 'ADMIN', 'OWNER', 'PRINCIPAL_ADMIN')")
    @DeleteMapping("/{questionId}")
    public ApiResponseEntity<Boolean> deleteQuestion(@PathVariable long questionId) {
        questionService.deleteQuestion(questionId);
        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }


//    @PreAuthorize("hasRole('APPROVED_SELLER')")
//    @GetMapping
//    public ApiResponseEntity<Page<SellerProductQuestionResponseDTO>> getQuestions(
//            @AuthenticationPrincipal CustomUserDetails user, @Valid GetProductsParams params, Pageable pageable) {
//        if (params.getStartDate() != null
//                && params.getEndDate() != null
//                && params.getEndDate().isBefore(params.getStartDate())) {
//            throw new CustomException(CommonErrorType.INVALID_END_DATE);
//        }
//        return ApiResponseEntity.of(questionService.getProducts(user.getId(), params, pageable));
//    }

}
