package com.impacus.maketplace.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductClaimInfo extends ClaimInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_claim_info_id")
    private Long id;

    @Column(nullable = false)
    private Long productId;

    public ProductClaimInfo(
            Long productId,
            String recallInfo,
            String claimCost,
            String claimPolicyGuild,
            String claimContactInfo
    ) {
        super(recallInfo, claimCost, claimPolicyGuild, claimContactInfo);
        this.productId = productId;
    }
}
