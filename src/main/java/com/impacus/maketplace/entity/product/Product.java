package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.DeliveryType;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@Table(name = "product_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE product_info SET is_deleted = TRUE WHERE product_info_id = ?")
@Where(clause = "is_deleted = false")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_info_id")
    private Long id;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Long sellerId;

    @Column(nullable = false, length = 50)
    private String name; // 상품명

    @Column(nullable = false)
    private String productNumber; // 상품 번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // 배송 타입

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryCompany deliveryCompany;

    @Column(nullable = false)
    @ColumnDefault("1")
    private Long categoryId; // 카테고리 id

    @Column(nullable = false)
    @ColumnDefault("'CHARGE_UNDER_30000'")
    @Enumerated(EnumType.STRING)
    @Comment("배송비 타입")
    private DeliveryRefundType deliveryFeeType;

    @Column(nullable = false)
    @ColumnDefault("'CHARGE_UNDER_30000'")
    @Enumerated(EnumType.STRING)
    @Comment("반송비 타입")
    private DeliveryRefundType refundFeeType;

    private int deliveryFee; // 배송비

    private int refundFee; // 반송비

    @Comment("특수 지역 배송비")
    private int specialDeliveryFee;

    @Comment("특수 지역 반품비")
    private int specialRefundFee;

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

    @ColumnDefault("'GENERAL'")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType type; // 상품 타입

    @ColumnDefault("''")
    @Column(nullable = false, columnDefinition = "TEXT")
    @Comment("상품 설명")
    private String description;


    public Product(String productNumber, Long sellerId, CreateProductDTO productRequest) {
        this.sellerId = sellerId;
        this.name = productRequest.getName();
        this.productNumber = productNumber;
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
        this.deliveryFeeType = productRequest.getDeliveryFeeType();
        this.refundFeeType = productRequest.getRefundFeeType();
    }

    public void setProduct(UpdateProductDTO dto) {
        this.name = dto.getName();
        this.deliveryType = dto.getDeliveryType();
        this.categoryId = dto.getCategoryId();
        this.deliveryFee = dto.getDeliveryFee();
        this.refundFee = dto.getRefundFee();
        this.marketPrice = dto.getMarketPrice();
        this.appSalesPrice = dto.getAppSalesPrice();
        this.discountPrice = dto.getDiscountPrice();
        this.weight = dto.getWeight();
        this.productStatus = dto.getProductStatus();
        this.type = dto.getType();
    }
}
