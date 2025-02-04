package com.impacus.maketplace.controller.qna;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.qna.response.ConsumerQuestionDTO;
import com.impacus.maketplace.service.qna.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user/questions")
public class UserQuestionController {
    private final QuestionService questionService;

    /**
     * 사용자 등록 문의 조회 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping
    public ApiResponseEntity<Slice<ConsumerQuestionDTO>> findConsumerQuestions(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 2, direction = Sort.Direction.DESC, sort = "createAt") Pageable pageable
    ) {
        Slice<ConsumerQuestionDTO> result = questionService.findConsumerQuestions(user.getId(), pageable);

        return ApiResponseEntity
                .<Slice<ConsumerQuestionDTO>>builder()
                .code(HttpStatus.OK)
                .message("사용자 등록 문의 조회")
                .data(result)
                .build();
    }
}
