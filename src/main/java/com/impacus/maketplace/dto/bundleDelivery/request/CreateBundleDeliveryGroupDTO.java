package com.impacus.maketplace.dto.bundleDelivery.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateBundleDeliveryGroupDTO {

    private Long sellerId;

    @NotBlank
    @Size(max = 25)
    private String name;

    private Boolean isUsed;

    @ValidEnum(enumClass = DeliveryFeeRule.class)
    private DeliveryFeeRule deliveryFeeRule;

    public BundleDeliveryGroup toEntity(
            Long sellerId
    ) {
        return new BundleDeliveryGroup(
                name,
                sellerId,
                isUsed,
                deliveryFeeRule
        );
    }
}
