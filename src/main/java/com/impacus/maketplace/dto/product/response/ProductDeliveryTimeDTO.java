package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.entity.product.ProductDeliveryTime;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDeliveryTimeDTO {
    private int minDays = -1;
    private int maxDays = -1;

    public ProductDeliveryTimeDTO(int minDays, int maxDays) {
        this.minDays = minDays;
        this.maxDays = maxDays;
    }

    @JsonIgnore
    public boolean isNull() {
        return this.minDays ==-1 && this.maxDays == -1;
    }

    public static ProductDeliveryTimeDTO toDTO(ProductDeliveryTime deliveryTime) {
        if (deliveryTime == null) {
            return null;
        }
        return new ProductDeliveryTimeDTO(deliveryTime.getMinDays(), deliveryTime.getMaxDays());
    }
}
