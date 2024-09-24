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

    /**
     * 주문 PK
     */
    private Long paymentEventId;

    @Column(nullable = false)
    private Long userId;

    private String contents;

    private Long attachFileId;

    private String userName;

    @Column(nullable = false)
    private Long sellerId;

    public ProductQuestion(Long productId, Long paymentEventId, Long userId, String contents, Long attachFileId) {
        this.productId = productId;
        this.paymentEventId = paymentEventId;
        this.userId = userId;
        this.contents = contents;
        this.attachFileId = attachFileId;
    }
}
