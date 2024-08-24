package com.impacus.maketplace.dto.bundleDelivery.response;

import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BundleDeliveryGroupDetailDTO {

    private Long groupId;
    private String groupNumber;
    private String name;
    private DeliveryFeeRule deliveryFeeRule;
    private Boolean isUsed;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;
}
