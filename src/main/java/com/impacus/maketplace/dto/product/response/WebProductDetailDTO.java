package com.impacus.maketplace.dto.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebProductDetailDTO {
    private Long productId;
    private String productNumber;
    private Long version;

    private WebProductBasicDTO information;
    private WebProductSpecificationDTO specification;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<WebProductOptionDetailDTO> productOptions;

    private ProductDetailInfoDTO productDetail;
    private ProductClaimInfoDTO claim;

    public void processNullObject() {
        if (this.information!=null && information.getDeliveryTime() !=null && information.getDeliveryTime().isNull()) {
            information.setDeliveryTime(null);
        }

        if (this.specification!= null && this.specification.isNull()) {
            this.specification = null;
        }

        if (this.productDetail !=null && this.productDetail.isNull()) {
            this.productDetail = null;
        }

        if (this.claim !=null && this.claim.isNull()) {
            this.claim = null;
        }
    }
}
