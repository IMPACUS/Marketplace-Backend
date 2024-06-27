package com.impacus.maketplace.dto.product.request;

import com.impacus.maketplace.entity.product.ProductClaimInfo;
import com.impacus.maketplace.entity.temporaryProduct.TemporaryProductClaimInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateClaimInfoDTO {
    @NotBlank
    private String recallInfo;

    @NotBlank
    private String claimCost;

    @NotBlank
    private String claimPolicyGuild;

    @NotBlank
    private String claimContactInfo;

    public ProductClaimInfo toEntity(Long productId) {
        return new ProductClaimInfo(
                productId,
                this.recallInfo,
                this.claimCost,
                this.claimPolicyGuild,
                this.claimContactInfo
        );
    }

    public TemporaryProductClaimInfo toTemporaryEntity(Long temporaryProductId) {
        return new TemporaryProductClaimInfo(
                temporaryProductId,
                this.recallInfo,
                this.claimCost,
                this.claimPolicyGuild,
                this.claimContactInfo
        );
    }
}
