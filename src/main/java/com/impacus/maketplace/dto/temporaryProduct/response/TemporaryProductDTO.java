package com.impacus.maketplace.dto.temporaryProduct.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.impacus.maketplace.dto.product.response.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
public class TemporaryProductDTO {

    @JsonIgnore
    private Long id;
    private WebProductBasicDTO information;
    private WebProductSpecificationDTO specification;

    @Setter
    private Set<WebProductOptionDetailDTO> productOptions;

    private ProductDetailInfoDTO productDetail;
    private ProductClaimInfoDTO claim;

    public void updateCategoryNull() {
        information.setCategoryId(null);
    }

    public void updateBundleDeliveryGroupNull() {
        information.setBundleDeliveryGroupId(null);
    }



}
