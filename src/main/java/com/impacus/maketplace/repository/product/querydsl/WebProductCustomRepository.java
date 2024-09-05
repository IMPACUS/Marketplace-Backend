package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.product.dto.CommonProductDTO;
import com.impacus.maketplace.dto.product.response.WebProductDTO;

public interface WebProductCustomRepository {
    CommonProductDTO findCommonProductByProductId(Long productId);

    WebProductDTO findProductByProductId(UserType userType, Long sellerId, Long productId);
}
