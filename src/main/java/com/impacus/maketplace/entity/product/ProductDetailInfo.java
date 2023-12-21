package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_detail_info")
public class ProductDetailInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_info_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productType; // 상품 종류

    @Column(nullable = false)
    private String productMaterial; // 상품 재고

    @Column(nullable = false)
    private String productColor; // 상품 색상

    @Column(nullable = false)
    private String productSize; // 상품 사이즈

    @Column(nullable = false)
    private String dateOfManufacture; // 제조일

    @Column(nullable = false)
    private String washingPrecautions; // 세탁 시 주의사항

    @Column(nullable = false)
    private String countryOfManufacture; // 제조국

    @Column(nullable = false)
    private String manufacturer; // 제조자

    @Column(nullable = false)
    private String importer; // 수입자

    @Column(nullable = false)
    private String qualityAssuranceStandards; // 품질보증기준

    @Column(nullable = false)
    private String asManager; // A/S 책임자

    @Column(nullable = false)
    private String contactNumber; // 전화번호
}
