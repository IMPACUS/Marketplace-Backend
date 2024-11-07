package com.impacus.maketplace.dto.bundleDelivery.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.impacus.maketplace.common.enumType.product.DeliveryFeeRule;
import com.impacus.maketplace.entity.product.bundleDelivery.BundleDeliveryGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BundleDeliveryGroupDetailDTO {

    private Long groupId;
    private String groupNumber;
    private String name;
    private DeliveryFeeRule deliveryFeeRule;

    @JsonProperty(value = "isUsed")
    private Boolean isUsed;
    private LocalDateTime createAt;
    private LocalDateTime modifyAt;

    public static BundleDeliveryGroupDetailDTO toDTO(BundleDeliveryGroup group) {
        return new BundleDeliveryGroupDetailDTO(
                group.getId(),
                group.getGroupNumber(),
                group.getName(),
                group.getDeliveryFeeRule(),
                group.isUsed(),
                group.getCreateAt(),
                group.getModifyAt()
        );
    }
}
