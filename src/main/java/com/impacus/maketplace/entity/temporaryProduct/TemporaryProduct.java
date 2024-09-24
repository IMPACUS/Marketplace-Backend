package com.impacus.maketplace.entity.temporaryProduct;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.ListToJsonConverter;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.DiscountStatus;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "temporary_product")
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_product_id")
    private Long id;

    @Column(length = 50)
    private String name; // 상품명

    @Column(name = "is_custom_product")
    private boolean isCustomProduct;

    @Column
    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType; // 배송 타입

    @Column
    private Long categoryId; // 2차 카테고리 id
    
    @Enumerated(EnumType.STRING)
    @Comment("배송비 타입")
    private DeliveryRefundType deliveryFeeType;

    @Enumerated(EnumType.STRING)
    @Comment("반송비 타입")
    private DeliveryRefundType refundFeeType;

    @Column(columnDefinition = "TEXT")
    private String description; // 상품 설명

    @Column
    private Integer deliveryFee; // 배송비

    @Column
    private Integer refundFee; // 반송비

    @Comment("특수 지역 배송비")
    private Integer specialDeliveryFee;

    @Comment("특수 지역 반품비")
    private Integer specialRefundFee;

    @Column
    private Integer marketPrice; // 시중 판매가

    @Column
    private Integer appSalesPrice; // 앱 판매가

    @Column
    private Integer discountPrice; // 할인가

    @Column
    private Integer weight; // 무게

    @ColumnDefault("'SALES_PROGRESS'")
    @Column
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus; // 상품 상태

    @Enumerated(EnumType.STRING)
    private ProductType type; // 상품 타입

    @Enumerated(EnumType.STRING)
    private DeliveryCompany deliveryCompany;

    @Convert(converter = ListToJsonConverter.class)
    @Column(columnDefinition = "TEXT")
    @Comment("상품 이미지")
    private List<String> productImages;

    @Comment("판매 수수료")
    private Integer salesChargePercent;

    @ColumnDefault("'INDIVIDUAL_SHIPPING_ONLY'")
    @Comment("묶음배송대상상품옵션")
    @Enumerated(EnumType.STRING)
    private BundleDeliveryOption bundleDeliveryOption;

    @Comment("묶음배송그룹아이디")
    private Long bundleDeliveryGroupId;

    public TemporaryProduct(OptionStepProductDTO dto) {
        this.weight = dto.getWeight();
        this.productStatus = dto.getProductStatus();
        this.description = dto.getDescription();
        this.type = dto.getType();
    }

    public TemporaryProduct(BasicStepProductDTO dto) {
        this.name = dto.getName();
        this.deliveryType = dto.getDeliveryType();
        this.isCustomProduct = dto.getIsCustomProduct();
        this.categoryId = dto.getCategoryId();
        this.salesChargePercent = dto.getSalesChargePercent();
        this.deliveryFeeType = dto.getDeliveryFeeType();
        this.refundFeeType = dto.getRefundFeeType();
        if (dto.getDeliveryFeeType() == DeliveryRefundType.MANUAL) {
            this.deliveryFee = dto.getDeliveryFee();
            this.specialDeliveryFee = dto.getSpecialDeliveryFee();
        }

        if (dto.getRefundFeeType() == DeliveryRefundType.MANUAL) {
            this.refundFee = dto.getRefundFee();
            this.specialRefundFee = dto.getSpecialRefundFee();
        }

        this.deliveryCompany = dto.getDeliveryCompany();
        this.bundleDeliveryOption = dto.getBundleDeliveryOption();
        this.bundleDeliveryGroupId = dto.getBundleDeliveryGroupId();
        this.productImages = dto.getProductImages();
        this.marketPrice = dto.getMarketPrice();
        this.appSalesPrice = dto.getAppSalesPrice();
        this.discountPrice = dto.getDiscountPrice();
    }

    public TemporaryProduct(CreateProductDTO dto) {
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
        this.productImages = dto.getProductImages();

        if (dto.getDeliveryFeeType() == DeliveryRefundType.MANUAL) {
            this.deliveryFee = dto.getDeliveryFee();
            this.specialDeliveryFee = dto.getSpecialDeliveryFee();
        }

        if (dto.getRefundFeeType() == DeliveryRefundType.MANUAL) {
            this.refundFee = dto.getRefundFee();
            this.specialRefundFee = dto.getSpecialRefundFee();
        }
    }

    public void setProduct(CreateProductDTO dto) {
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
        this.productImages = dto.getProductImages();

        if (dto.getDeliveryFeeType() == DeliveryRefundType.MANUAL) {
            this.deliveryFee = dto.getDeliveryFee();
            this.specialDeliveryFee = dto.getSpecialDeliveryFee();
        }

        if (dto.getRefundFeeType() == DeliveryRefundType.MANUAL) {
            this.refundFee = dto.getRefundFee();
            this.specialRefundFee = dto.getSpecialRefundFee();
        }
    }
}
