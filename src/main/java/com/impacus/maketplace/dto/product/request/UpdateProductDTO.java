package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.DeliveryType;
import com.impacus.maketplace.common.enumType.product.DeliveryRefundType;
import com.impacus.maketplace.common.enumType.product.ProductStatus;
import com.impacus.maketplace.common.enumType.product.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDTO {
    @NotNull
    private Long productId;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    private String description;

    @ValidEnum(enumClass = DeliveryType.class)
    private DeliveryType deliveryType;

    @ValidEnum(enumClass = DeliveryCompany.class)
    private DeliveryCompany deliveryCompany;

    @NotNull
    private Long categoryId;

    private Integer deliveryFee;

    private Integer refundFee;

    @NotNull
    @ValidEnum(enumClass = DeliveryRefundType.class)
    private DeliveryRefundType deliveryFeeType;

    @NotNull
    @ValidEnum(enumClass = DeliveryRefundType.class)
    private DeliveryRefundType refundFeeType;

    private Integer specialDeliveryFee;

    private Integer specialRefundFee;

    @NotNull
    private int marketPrice;

    private int appSalesPrice;

    private int discountPrice;

    @NotNull
    private int weight;

    @NotNull
    private CreateProductDetailInfoDTO productDetail;

    @NotNull
    private List<CreateProductOptionDTO> productOptions;

    @NotNull
    private CreateProductDeliveryTimeDTO deliveryTime;

    @NotNull
    private CreateClaimInfoDTO claim;

    @ValidEnum(enumClass = ProductStatus.class)
    private ProductStatus productStatus;

    @ValidEnum(enumClass = ProductType.class)
    private ProductType type;
}
