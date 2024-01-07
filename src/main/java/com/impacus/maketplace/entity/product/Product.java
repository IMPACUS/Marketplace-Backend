package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.ProductStatus;
import com.impacus.maketplace.common.enumType.category.SubCategory;
import com.impacus.maketplace.dto.product.request.ProductRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@Table(name = "product_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE product_info SET is_deleted = true WHERE product_info_id = ?")
@Where(clause = "is_deleted = false")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_id")
    private Long id;

    @Column(nullable = false)
    private Long brandId;

    @Column(nullable = false, length = 50)
    private String name; // 상품명

    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productNumber; // 상품 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // 배송 타입

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @ColumnDefault("'SALES_PROGRESS'")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus; // 상품 상태

    @ColumnDefault("'DISCOUNT_STOP'")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountStatus discountStatus; // 할인 상태

    public Product(String productNumber, ProductRequest productRequest) {
        this.brandId = productRequest.getBrandId();
        this.name = productRequest.getName();
        this.productNumber = productNumber;
        this.deliveryType = productRequest.getDeliveryType();
        this.categoryType = productRequest.getCategoryType();
        this.deliveryFee = productRequest.getDeliveryFee();
        this.refundFee = productRequest.getRefundFee();
        this.marketPrice = productRequest.getMarketPrice();
        this.appSalesPrice = productRequest.getAppSalesPrice();
        this.discountPrice = productRequest.getDiscountPrice();
        this.weight = productRequest.getWeight();
        this.productStatus = ProductStatus.SALES_PROGRESS;
        this.discountStatus = DiscountStatus.DISCOUNT_STOP;
    }

    public void setProduct(ProductRequest productRequest) {
        this.brandId = productRequest.getBrandId();
        this.name = productRequest.getName();
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
