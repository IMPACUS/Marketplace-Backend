package com.impacus.maketplace.entity.qna;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 상품 문의 (판매자 문의)
 */
@Entity
@NoArgsConstructor
@Getter
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @Column(nullable = false)
    private Long productOptionId;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private Long userId;

    private String contents;

    @Column(columnDefinition = "TEXT", name = "images")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> images;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public Question(
            Long orderId,
            Long productOptionId,
            Long userId,
            String contents,
            List<String> images
    ) {
        this.orderId = orderId;
        this.productOptionId = productOptionId;
        this.userId = userId;
        this.contents = contents;
        this.images = images;
    }
}
