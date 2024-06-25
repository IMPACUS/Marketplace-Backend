package com.impacus.maketplace.entity.temporaryProduct;

import com.impacus.maketplace.entity.product.ClaimInfo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TemporaryProductClaimInfo extends ClaimInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temporary_product_claim_info_id")
    private Long id;

    @Column(nullable = false)
    private Long temporaryProductId;
}
