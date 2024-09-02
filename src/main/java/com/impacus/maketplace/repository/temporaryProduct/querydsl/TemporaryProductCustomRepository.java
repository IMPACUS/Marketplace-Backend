package com.impacus.maketplace.repository.temporaryProduct.querydsl;

import com.impacus.maketplace.dto.product.request.BasicStepProductDTO;
import com.impacus.maketplace.dto.product.request.CreateProductDetailInfoDTO;
import com.impacus.maketplace.dto.product.request.OptionStepProductDTO;
import com.impacus.maketplace.dto.product.response.ProductDetailForWebDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TemporaryProductCustomRepository  {
    void updateTemporaryProduct(Long temporaryProductId, BasicStepProductDTO dto);

    void updateTemporaryProductDetail(Long temporaryProductId, CreateProductDetailInfoDTO dto);

    void updateTemporaryProductAtOptions(Long temporaryProductId, OptionStepProductDTO dto);

    void deleteRelationEntityById(Long temporaryProductId);

    ProductDetailForWebDTO findDetailIdByRegisterId(String string);
}
