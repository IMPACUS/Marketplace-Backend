package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@Table(name = "product_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_id")
    private Long id;

    @Column(nullable = false)
    private Long brandId;

//    private List<Long> productImageId = new ArrayList<>(); // 상품 대표 이미지 리스트 -> attachFile id 참조

    @Column(nullable = false, length = 50)
    private String name; // 상품명

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productNumber; // 상품 번호

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; // 상품 설명

    @ColumnDefault("1")
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private DeliveryType deliveryType; // 배송 타입

    @ColumnDefault("1")
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private SubCategory categoryType; // 카테고리 타입

    @Column(nullable = false)
    private int deliveryFee; // 배송비

    @Column(nullable = false)
    private int refundFee; // 반송비

    @Column(nullable = false)
    private int marketPrice; // 시중 판매가

    @Column(nullable = false)
    private int appSalesPrice; // 앱 판매가

    @Column(nullable = false)
    private int discountPrice; // 할인가

    @Column(nullable = false)
    private int weight; // 무게

    public Product(String productNumber, ProductRequest productRequest) {
        this.brandId = productRequest.getBrandId();
        this.name = productRequest.getName();
        this.productNumber = productNumber;
        this.description = productRequest.getDescription();
        this.deliveryType = productRequest.getDeliveryType();
        this.categoryType = productRequest.getCategoryType();
        this.deliveryFee = productRequest.getDeliveryFee();
        this.refundFee = productRequest.getRefundFee();
        this.marketPrice = productRequest.getMarketPrice();
        this.appSalesPrice = productRequest.getAppSalesPrice();
        this.discountPrice = productRequest.getDiscountPrice();
        this.weight = productRequest.getWeight();
    }

}
