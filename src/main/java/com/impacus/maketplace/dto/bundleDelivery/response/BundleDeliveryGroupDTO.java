package com.impacus.maketplace.dto.bundleDelivery.response;

import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BundleDeliveryGroupDTO {
    private Long groupId;
    private String groupNumber;
    private String name;
    private DeliveryFeeRule deliveryFeeRule;
}
