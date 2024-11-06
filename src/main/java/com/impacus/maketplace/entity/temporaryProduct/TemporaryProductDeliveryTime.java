package com.impacus.maketplace.entity.temporaryProduct;

import com.impacus.maketplace.entity.product.DeliveryTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TemporaryProductDeliveryTime extends DeliveryTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_product_delivery_time_id")
    private Long id;

    @Column(nullable = false)
    private Long temporaryProductId;

    public TemporaryProductDeliveryTime(
            Long temporaryProductId,
            int minDays,
            int maxDays) {
        super(minDays, maxDays);
        this.temporaryProductId = temporaryProductId;
    }

    public TemporaryProductDeliveryTime(
            Long temporaryProductId
    ) {
        this(temporaryProductId, -1, -1);
    }
}
