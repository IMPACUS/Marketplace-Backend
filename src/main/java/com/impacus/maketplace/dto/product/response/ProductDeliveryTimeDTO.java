package com.impacus.maketplace.dto.product.response;

import com.impacus.maketplace.entity.product.ProductDeliveryTime;
import lombok.Data;

@Data
public class ProductDeliveryTimeDTO {
    private int minDays;
    private int maxDays;

    public ProductDeliveryTimeDTO(int minDays, int maxDays) {
        this.minDays = minDays;
        this.maxDays = maxDays;
    }

    public static ProductDeliveryTimeDTO toDTO(ProductDeliveryTime deliveryTime) {
        return new ProductDeliveryTimeDTO(deliveryTime.getMinDays(), deliveryTime.getMaxDays());
    }
}
