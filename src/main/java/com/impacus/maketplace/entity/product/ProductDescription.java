package com.impacus.maketplace.entity.product;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_description_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // 상품 설명

    // TODO 상품 설명 이미지 리스트 -> attachFile id 참조
}
