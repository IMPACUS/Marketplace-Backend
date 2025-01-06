package com.impacus.maketplace.controller.review;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.review.request.ReviewReplyDTO;
import com.impacus.maketplace.entity.review.Review;
import com.impacus.maketplace.service.review.ReviewReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class ReviewReplyController {
    private final ReviewReplyService reviewReplyService;

    /**
     * 리뷰 답변 등록 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PostMapping("/{reviewId}/reply")
    public ApiResponseEntity<Review> addReviewReply(
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ReviewReplyDTO dto) {
        reviewReplyService.addReviewReply(reviewId, dto);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.CREATED)
                .message("리뷰 답변 생성 성공")
                .build();
    }

    /**
     * 리뷰 답변 수정 API
     *
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER') " +
            "or hasRole('ROLE_ADMIN') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')" +
            "or hasRole('ROLE_OWNER')")
    @PutMapping("/reply/{reviewReplyId}")
    public ApiResponseEntity<Review> updateReviewReply(
            @PathVariable(name = "reviewReplyId") Long reviewReplyId,
            @Valid @RequestBody ReviewReplyDTO dto) {
        reviewReplyService.updateReviewReply(reviewReplyId, dto);

        return ApiResponseEntity
                .<Review>builder()
                .code(HttpStatus.CREATED)
                .message("리뷰 답변 수정 성공")
                .build();
    }
}
