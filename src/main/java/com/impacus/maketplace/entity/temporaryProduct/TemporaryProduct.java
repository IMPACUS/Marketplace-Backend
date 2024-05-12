package com.impacus.maketplace.entity.temporaryProduct;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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
    private Long userId;

    @Column(length = 50)
    private String name; // 상품명

    @Column
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // 배송 타입

    @Column
    private Long categoryId; // 2차 카테고리 id

    @Column
    private int deliveryFee; // 배송비

    @Column
    private int refundFee; // 반송비

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

    public TemporaryProduct(ProductRequest productRequest) {
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
    }

    public void setProduct(ProductRequest productRequest) {
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
    }
}
