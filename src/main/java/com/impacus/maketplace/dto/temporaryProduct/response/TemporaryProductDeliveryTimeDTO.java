package com.impacus.maketplace.dto.temporaryProduct.response;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDeliveryTime;
import lombok.Data;

@Data
public class TemporaryProductDeliveryTimeDTO {
    private int minDays;
    private int maxDays;

    public TemporaryProductDeliveryTimeDTO(int minDays, int maxDays) {
        this.minDays = minDays;
        this.maxDays = maxDays;
    }

    public static TemporaryProductDeliveryTimeDTO toDTO(TemporaryProductDeliveryTime deliveryTime) {
        return new TemporaryProductDeliveryTimeDTO(deliveryTime.getMinDays(), deliveryTime.getMaxDays());
    }
}
