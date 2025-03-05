package com.impacus.maketplace.controller.qna;

import com.impacus.maketplace.common.enumType.searchCondition.QnAReviewSearchCondition;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.dto.qna.request.CreateQuestionDTO;
import com.impacus.maketplace.dto.qna.response.WebQuestionDTO;
import com.impacus.maketplace.dto.qna.response.WebQuestionDetailDTO;
import com.impacus.maketplace.dto.review.QnaReviewSearchCondition;
import com.impacus.maketplace.service.qna.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.time.LocalDate;
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

    /**
     * 문의 조회
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @GetMapping
    public ApiResponseEntity<Page<WebQuestionDTO>> findQuestions(
            @PageableDefault(size = 5, direction = Sort.Direction.DESC, sort = "createAt") Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "start-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
            @RequestParam(value = "end-at", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
            @RequestParam(value = "search-condition", required = true) QnAReviewSearchCondition searchCondition
    ) {
        QnaReviewSearchCondition condition = new QnaReviewSearchCondition(
                pageable, keyword, startAt, endAt, searchCondition
        );
        Page<WebQuestionDTO> result = questionService.findQuestions(condition);

        return ApiResponseEntity
                .<Page<WebQuestionDTO>>builder()
                .code(HttpStatus.OK)
                .message("문의 조회 성공")
                .data(result)
                .build();
    }

    /**
     * 문의 단건 조회
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @GetMapping("/{questionId}")
    public ApiResponseEntity<WebQuestionDetailDTO> findQuestion(
            @PathVariable(name = "questionId") Long questionId
    ) {
        WebQuestionDetailDTO result = questionService.findQuestion(
                questionId
        );

        return ApiResponseEntity
                .<WebQuestionDetailDTO>builder()
                .code(HttpStatus.OK)
                .message("문의 단건 조회 성공")
                .data(result)
                .build();
    }

    /**
     * [관리자, 판매자] 문의 엑셀 다운
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PostMapping("/excel")
    public ApiResponseEntity<FileGenerationStatusIdDTO> exportQuestions(
            @RequestBody IdsDTO dto
    ) {
        FileGenerationStatusIdDTO result = questionService.exportQuestions(
                dto
        );

        return ApiResponseEntity
                .<FileGenerationStatusIdDTO>builder()
                .code(HttpStatus.OK)
                .message("문의 엑셀 다운")
                .data(result)
                .build();
    }

}
