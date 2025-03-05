package com.impacus.maketplace.controller.qna;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.qna.request.QuestionReplyDTO;
import com.impacus.maketplace.service.qna.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/question")
public class QuestionReplyController {
    private final QuestionService questionService;

    /**
     * 문의 답변 등록 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PostMapping("/{questionId}/reply")
    public ApiResponseEntity<Boolean> addReviewReply(
            @PathVariable(name = "questionId") Long questionId,
            @Valid @RequestBody QuestionReplyDTO dto) {
        questionService.addQuestionReply(questionId, dto);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }
}
