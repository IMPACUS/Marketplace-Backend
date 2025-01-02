package com.impacus.maketplace.entity.review;


import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.ListToJsonConverter;
import com.impacus.maketplace.common.converter.MapToJsonConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.Map;

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
    @Convert(converter = MapToJsonConverter.class)
    private Map<Long, String> images;

    @Column(name = "rating", nullable = false)
    private float rating;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부
}
