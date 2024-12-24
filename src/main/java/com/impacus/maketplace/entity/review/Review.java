package com.impacus.maketplace.entity.review;


import com.impacus.maketplace.common.converter.ListToJsonConverter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Review {
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
}
