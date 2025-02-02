package com.impacus.maketplace.entity.qna;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 문의에 대한 답변
 */
@Entity
@NoArgsConstructor
@Getter
public class QuestionReply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_reply_id")
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    private String contents;

}
