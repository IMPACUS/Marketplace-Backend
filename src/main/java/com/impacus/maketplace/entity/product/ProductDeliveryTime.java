package com.impacus.maketplace.entity.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductDeliveryTime extends DeliveryTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_delivery_time_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    public ProductDeliveryTime(
            Long productId,
            int minDays,
            int maxDays
    ) {
        super(minDays, maxDays);
        this.productId = productId;
    }
}
