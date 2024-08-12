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

    private Long orderId;

    @Column(nullable = false)
    private Long userId;

    private String contents;

    private Long attachFileId;

    public ProductQuestion(Long productId, Long orderId, Long userId, String contents, Long attachFileId) {
        this.productId = productId;
        this.orderId = orderId;
        this.userId = userId;
        this.contents = contents;
        this.attachFileId = attachFileId;
    }
}
