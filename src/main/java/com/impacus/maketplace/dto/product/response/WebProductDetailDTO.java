package com.impacus.maketplace.dto.product.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
public class WebProductDetailDTO {
    private Long productId;
    private String productNumber;
    private Long version;

    private WebProductBasicDTO information;
    private WebProductSpecificationDTO specification;

    @Setter
    private Set<WebProductOptionDetailDTO> productOptions;

    private ProductDetailInfoDTO productDetail;
    private ProductClaimInfoDTO claim;


}
