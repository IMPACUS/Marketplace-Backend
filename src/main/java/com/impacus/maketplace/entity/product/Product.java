package com.impacus.maketplace.entity.product;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.ListToJsonConverter;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.request.UpdateProductDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Table(name = "product_info")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = false, unique = true)
    private String productNumber; // 상품 번호

    @Convert(converter = ListToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    @Comment("상품 이미지")
    private List<String> productImages;

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

    private Integer deliveryFee; // 배송비

    private Integer refundFee; // 반송비

    @Comment("특수 지역 배송비")
    private Integer specialDeliveryFee;

    @Comment("특수 지역 반품비")
    private Integer specialRefundFee;

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

    @Comment("판매 수수료")
    private Integer salesChargePercent; // null 인 경우, 판매자의 수수료를 사용

    @ColumnDefault("'INDIVIDUAL_SHIPPING_ONLY'")
    @Comment("묶음배송대상상품옵션")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BundleDeliveryOption bundleDeliveryOption;

    @Comment("묶음배송그룹아이디")
    private Long bundleDeliveryGroupId;

    @Comment("묶음 배송 대상 상품 옵션 적용 날짜")
    private LocalDateTime bundleDeliveryOptionAppliedAt;

    @Version
    private long version;

    public Product(String productNumber, Long sellerId, CreateProductDTO dto) {
        this.sellerId = sellerId;
        this.name = dto.getName();
        this.productNumber = productNumber;
        this.deliveryType = dto.getDeliveryType();
        this.deliveryCompany = dto.getDeliveryCompany();
        this.categoryId = dto.getCategoryId();
        this.deliveryFeeType = dto.getDeliveryFeeType();
        this.refundFeeType = dto.getRefundFeeType();
        this.marketPrice = dto.getMarketPrice();
        this.appSalesPrice = dto.getAppSalesPrice();
        this.discountPrice = dto.getDiscountPrice();
        this.weight = dto.getWeight();
        this.isDeleted = false;
        this.productStatus = dto.getProductStatus();
        this.discountStatus = DiscountStatus.DISCOUNT_PROGRESS; // TODO 삭제
        this.type = dto.getType();
        this.description = dto.getDescription();
        this.productImages = dto.getProductImages();

        if (dto.getDeliveryFeeType() == DeliveryRefundType.CHARGE_UNDER_30000) {
            this.deliveryFee = dto.getDeliveryFee();
            this.specialDeliveryFee = dto.getSpecialDeliveryFee();
        }

        if (dto.getRefundFeeType() == DeliveryRefundType.CHARGE_UNDER_30000) {
            this.refundFee = dto.getRefundFee();
            this.specialRefundFee = dto.getSpecialRefundFee();
        }
    }

    public void setProduct(UpdateProductDTO dto) {
        this.name = dto.getName();
        this.deliveryType = dto.getDeliveryType();
        this.deliveryCompany = dto.getDeliveryCompany();
        this.categoryId = dto.getCategoryId();
        this.deliveryFeeType = dto.getDeliveryFeeType();
        this.refundFeeType = dto.getRefundFeeType();
        this.marketPrice = dto.getMarketPrice();
        this.appSalesPrice = dto.getAppSalesPrice();
        this.discountPrice = dto.getDiscountPrice();
        this.weight = dto.getWeight();
        this.productStatus = dto.getProductStatus();
        this.type = dto.getType();
        this.description = dto.getDescription();

        if (dto.getDeliveryFeeType() == DeliveryRefundType.CHARGE_UNDER_30000) {
            this.deliveryFee = dto.getDeliveryFee();
            this.specialDeliveryFee = dto.getSpecialDeliveryFee();
        }

        if (dto.getRefundFeeType() == DeliveryRefundType.CHARGE_UNDER_30000) {
            this.refundFee = dto.getRefundFee();
            this.specialRefundFee = dto.getSpecialRefundFee();
        }
    }
}
