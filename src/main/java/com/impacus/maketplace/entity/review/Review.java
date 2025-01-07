package com.impacus.maketplace.entity.review;


import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "product_option_id", nullable = false)
    private Long productOptionId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(columnDefinition = "TEXT", name = "images")
    @Convert(converter = ListToJsonConverter.class)
    private List<String> images;

    @Column(name = "rating", nullable = false)
    private float rating;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Builder
    public Review(
            Long orderId,
            Long productOptionId,
            Long userId,
            String contents,
            List<String> images,
            float rating
    ) {
        this.orderId = orderId;
        this.productOptionId = productOptionId;
        this.userId = userId;
        this.contents = contents;
        this.images = images;
        this.rating = rating;
    }
}
