package com.impacus.maketplace.dto.temporaryProduct.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.impacus.maketplace.dto.product.response.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemporaryProductDTO {

    @JsonIgnore
    private Long id;

    private WebProductBasicDTO information;

    private WebProductSpecificationDTO specification;

    @Setter
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<WebProductOptionDetailDTO> productOptions;

    private ProductDetailInfoDTO productDetail;

    private ProductClaimInfoDTO claim;

    public void updateCategoryNull() {
        information.setCategoryId(null);
    }

    public void updateBundleDeliveryGroupNull() {
        information.setBundleDeliveryGroupId(null);
    }

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
