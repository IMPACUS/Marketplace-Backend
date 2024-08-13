package com.impacus.maketplace.entity.temporaryProduct;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "temporary_product_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_product_info_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private Long sellerId; // 판매자 id

    @Column(length = 50)
    private String name; // 상품명

    @Column
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // 배송 타입

    @Column
    private Long categoryId; // 2차 카테고리 id

    @ColumnDefault("'CHARGE_UNDER_30000'")
    @Enumerated(EnumType.STRING)
    @Comment("배송비 타입")
    private DeliveryRefundType deliveryFeeType;

    @ColumnDefault("'CHARGE_UNDER_30000'")
    @Enumerated(EnumType.STRING)
    @Comment("반송비 타입")
    private DeliveryRefundType refundFeeType;

    @Column(columnDefinition = "TEXT")
    private String description; // 상품 설명

    @Column
    private int deliveryFee; // 배송비

    @Column
    private int refundFee; // 반송비

    @Comment("특수 지역 배송비")
    private int specialDeliveryFee;

    @Comment("특수 지역 반품비")
    private int specialRefundFee;

    @Column
    private int marketPrice; // 시중 판매가

    @Column
    private int appSalesPrice; // 앱 판매가

    @Column
    private int discountPrice; // 할인가

    @Column
    private int weight; // 무게

    @ColumnDefault("'SALES_PROGRESS'")
    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus; // 상품 상태

    @ColumnDefault("'DISCOUNT_STOP'")
    @Column
    @Enumerated(EnumType.STRING)
    private DiscountStatus discountStatus; // 할인 상태

    @Enumerated(EnumType.STRING)
    private ProductType type; // 상품 타입

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryCompany deliveryCompany;

    public TemporaryProduct(Long sellerId, CreateProductDTO productRequest) {
        this.sellerId = sellerId;
        this.name = productRequest.getName();
        this.deliveryType = productRequest.getDeliveryType();
        this.categoryId = productRequest.getCategoryId();
        this.deliveryFee = productRequest.getDeliveryFee();
        this.refundFee = productRequest.getRefundFee();
        this.marketPrice = productRequest.getMarketPrice();
        this.appSalesPrice = productRequest.getAppSalesPrice();
        this.discountPrice = productRequest.getDiscountPrice();
        this.weight = productRequest.getWeight();
        this.productStatus = productRequest.getProductStatus();
        this.discountStatus = DiscountStatus.DISCOUNT_STOP;
        this.type = productRequest.getType();
        this.description = productRequest.getDescription();
    }

    public void setProduct(CreateProductDTO productRequest) {
        this.name = productRequest.getName();
        this.deliveryType = productRequest.getDeliveryType();
        this.categoryId = productRequest.getCategoryId();
        this.deliveryFee = productRequest.getDeliveryFee();
        this.refundFee = productRequest.getRefundFee();
        this.marketPrice = productRequest.getMarketPrice();
        this.appSalesPrice = productRequest.getAppSalesPrice();
        this.discountPrice = productRequest.getDiscountPrice();
        this.weight = productRequest.getWeight();
        this.productStatus = productRequest.getProductStatus();
        this.type = productRequest.getType();
        this.description = productRequest.getDescription();
    }
}
