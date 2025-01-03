package com.impacus.maketplace.service.review;

import com.impacus.maketplace.common.enumType.error.ReviewErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.review.request.ReviewReplyDTO;
import com.impacus.maketplace.entity.review.ReviewReply;
import com.impacus.maketplace.repository.review.ReviewReplyRepository;
import com.impacus.maketplace.repository.review.ReviewRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewReplyService {
    private final ReviewReplyRepository reviewReplyRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 답변 등록
     *
     * @param reviewId
     * @param dto
     */
    @Transactional
    public void addReviewReply(Long reviewId, @Valid ReviewReplyDTO dto) {
        try {
            // 유효성 확인
            validateReviewReply(reviewId);

            // 저장
            ReviewReply reviewReply = dto.toEntity(reviewId);
            reviewReplyRepository.save(reviewReply);
        } catch (CustomException ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 리뷰 답변 유효성 검사
     *
     * @param reviewId
     */
    private void validateReviewReply(Long reviewId) {
        // 존재하는 리뷰인지 확인
        if (reviewRepository.existsByIdAndIsDeletedFalse(reviewId)) {
            throw new CustomException(ReviewErrorType.NOT_EXISTED_REVIEW_ID);
        }

        // 리뷰 답변이 이미 존재하는지 확인
        if(reviewReplyRepository.existsByReviewId(reviewId)) {
            throw new CustomException(ReviewErrorType.EXISTED_REVIEW_REPLY);
        }
    }
}
