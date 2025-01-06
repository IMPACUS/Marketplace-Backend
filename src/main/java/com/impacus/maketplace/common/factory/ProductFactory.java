package com.impacus.maketplace.common.factory;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.common.enumType.product.*;
import com.impacus.maketplace.dto.product.request.*;

import java.util.ArrayList;
import java.util.List;

public final class ProductFactory {
    public static CreateProductDTO createProductDTO(String name) {
        List<CreateProductOptionDTO> options = new ArrayList<>();
        options.add(new CreateProductOptionDTO(null, "Blue", "M", 100L));

        List<String> images = new ArrayList<>();
        images.add("https://dev-impacus-s3.s3.ap-northeast-2.amazonaws.com/copyFile/24121900003.jpeg");

        return new CreateProductDTO(
                1L,
                false,
                name,
                DeliveryType.GENERAL_DELIVERY,
                true,
                1L,
                DeliveryRefundType.FREE_SHIPPING,
                DeliveryRefundType.FREE_SHIPPING,
                null,
                null,
                null,
                null,
                DeliveryCompany.CJ,
                BundleDeliveryOption.INDIVIDUAL_SHIPPING_ONLY,
                null,
                images,
                100000,
                100000,
                100000,
                100,
                ProductStatus.SALES_PROGRESS,
                "",
                ProductType.GENERAL,
                10,
                new CreateProductDetailInfoDTO(
                        "productType",
                        "productMaterial",
                        "productColor",
                        "productSize",
                        "dateOfManufacture",
                        "washingPrecautions",
                        "countryOfManufacture",
                        "manufacturer",
                        "importer",
                        "quantityAssuranceStandards",
                        "asManager",
                        "contactNumber"
                ),
                options,
                new CreateProductDeliveryTimeDTO(0, 0),
                new CreateClaimInfoDTO(
                        "recallInfo",
                        "claimCost",
                        "claimPolicyGuild",
                        "claimContactInfo"
                ));
    }
}
