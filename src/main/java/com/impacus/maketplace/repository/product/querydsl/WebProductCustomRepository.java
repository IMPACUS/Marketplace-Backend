package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.dto.product.dto.CommonProductDTO;

public interface WebProductCustomRepository {
    CommonProductDTO findProductByProductId(Long productId);
}
