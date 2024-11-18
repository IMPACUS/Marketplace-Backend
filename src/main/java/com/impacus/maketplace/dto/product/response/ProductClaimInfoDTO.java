package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.common.enumType.product.ProductClaimInfo;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    public static ProductClaimInfoDTO toDTO() {
        return new ProductClaimInfoDTO(
                ProductClaimInfo.RECALL_INFO.getContent(),
                ProductClaimInfo.COST.getContent(),
                ProductClaimInfo.POLICY_GUIDE.getContent(),
                ProductClaimInfo.CONTACT_INFO.getContent()
        );
    }

    @JsonIgnore
    public boolean isNull() {
        return recallInfo.isBlank() && claimCost.isBlank()  && claimPolicyGuild.isBlank() && claimContactInfo.isBlank() ;
    }
}
