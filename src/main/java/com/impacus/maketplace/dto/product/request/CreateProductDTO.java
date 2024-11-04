package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.entity.product.Product;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProduct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDTO extends ProductDTO {
    private Long sellerId;

    @NotNull
    private boolean doesUseTemporaryProduct;

    @NotNull
    private List<String> productImages;

    public Product toEntity(Long sellerId) {
        return new Product(sellerId, this);
    }

    public TemporaryProduct toTemporaryEntity() {
        return new TemporaryProduct(this);
    }

    public CreateProductDTO(
            Long sellerId,
            boolean doesUseTemporaryProduct,
            String name,
            DeliveryType deliveryType,
            Boolean isCustomProduct,
            Long categoryId,
            DeliveryRefundType deliveryFeeType,
            DeliveryRefundType refundFeeType,
            Integer deliveryFee,
            Integer refundFee,
            Integer specialDeliveryFee,
            Integer specialRefundFee,
            DeliveryCompany deliveryCompany,
            BundleDeliveryOption bundleDeliveryOption,
            Long bundleDeliveryGroupId,
            List<String> productImages,
            Integer marketPrice,
            Integer appSalesPrice,
            Integer discountPrice,
            Integer weight,
            ProductStatus productStatus,
            String description,
            ProductType type,
            Integer salesChargePercent,
            CreateProductDetailInfoDTO productDetail,
            List<CreateProductOptionDTO> productOptions,
            CreateProductDeliveryTimeDTO deliveryTime,
            CreateClaimInfoDTO claim
    ) {
        super(
                name, deliveryType, isCustomProduct, categoryId, deliveryFeeType, refundFeeType,
                deliveryFee, refundFee, specialDeliveryFee, specialRefundFee, deliveryCompany,
                bundleDeliveryOption, bundleDeliveryGroupId, marketPrice, appSalesPrice, discountPrice,
                weight, productStatus, description, type, salesChargePercent, productDetail,
                productOptions, deliveryTime, claim
        );
        this.sellerId = sellerId;
        this.doesUseTemporaryProduct = doesUseTemporaryProduct;
        this.productImages = productImages;
    }

}
