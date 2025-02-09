package com.impacus.maketplace.repository.qna.querydsl;

import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.qna.response.ConsumerQuestionDTO;
import com.impacus.maketplace.dto.qna.response.WebQuestionDTO;
import com.impacus.maketplace.dto.qna.response.WebQuestionDetailDTO;
import com.impacus.maketplace.dto.review.QnaReviewSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface QuestionCustomRepository {
    void deleteQuestionById(long questionId);

    Slice<ConsumerQuestionDTO> findConsumerQuestions(Long id, Pageable pageable);

    Page<WebQuestionDTO> findQuestions(QnaReviewSearchCondition condition);

    WebQuestionDetailDTO findQuestion(Long questionId);

    List<WebQuestionDTO> findQuestionsByIds(IdsDTO dto);
}
