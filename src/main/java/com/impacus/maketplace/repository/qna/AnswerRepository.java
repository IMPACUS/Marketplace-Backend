package com.impacus.maketplace.repository.qna;

import com.impacus.maketplace.entity.qna.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
