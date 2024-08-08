package com.impacus.maketplace.entity.qna;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 상품 문의 (판매자 문의)
 */
@Entity
@NoArgsConstructor
@Getter
public class ProductQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_question_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long userId;

    private String contents;

    private Long attachFileId;

}
