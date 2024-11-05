package com.impacus.maketplace.dto.temporaryProduct.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.impacus.maketplace.dto.product.response.ProductClaimInfoDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailInfoDTO;
import com.impacus.maketplace.dto.product.response.WebProductOptionDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
public class TemporaryProductDTO {

    @JsonIgnore
    private Long id;
    private TemporaryProductBasicDTO information;
    private TemporaryProductSpecificationDTO specification;

    @Setter
    private Set<WebProductOptionDetailDTO> productOptions;

    private ProductDetailInfoDTO productDetail;
    private ProductClaimInfoDTO claim;



}
