package com.impacus.maketplace.dto.product.response;

import lombok.Data;

@Data
public class ProductClaimInfoDTO {

    private String recallInfo;
    private String claimCost;
    private String claimPolicyGuild;
    private String claimContactInfo;

    public ProductClaimInfoDTO(
            String recallInfo,
            String claimCost,
            String claimPolicyGuild,
            String claimContactInfo
    ) {
        this.recallInfo = recallInfo;
        this.claimCost = claimCost;
        this.claimPolicyGuild = claimPolicyGuild;
        this.claimContactInfo = claimContactInfo;
    }
}
