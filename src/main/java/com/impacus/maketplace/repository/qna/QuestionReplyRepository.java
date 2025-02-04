package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.entity.qna.QuestionReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionReplyRepository extends JpaRepository<QuestionReply, Long> {
    boolean existsByQuestionId(Long questionId);
}
