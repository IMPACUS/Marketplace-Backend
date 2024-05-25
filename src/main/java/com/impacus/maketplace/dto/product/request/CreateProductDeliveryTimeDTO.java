package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductDeliveryTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDeliveryTimeDTO {
    @NotNull
    private Integer minDays;
    @NotNull
    private Integer maxDays;

    public TemporaryProductDeliveryTime toTemporaryEntity(Long temporaryProductId) {
        return new TemporaryProductDeliveryTime(
                temporaryProductId,
                this.minDays == null ? 0 : this.minDays,
                this.maxDays == null ? 0 : this.maxDays
        );
    }
}
