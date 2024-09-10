package com.impacus.maketplace.repository.product.querydsl;

import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.dto.product.dto.CommonProductDTO;
import com.impacus.maketplace.dto.product.response.WebProductDTO;
import com.impacus.maketplace.dto.product.response.WebProductTableDTO;
import com.impacus.maketplace.dto.product.response.WebProductTableDetailDTO;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebProductCustomRepository {
    CommonProductDTO findCommonProductByProductId(Long productId);

    WebProductDTO findProductByProductId(UserType userType, Long sellerId, Long productId);

    Page<WebProductTableDTO> findProductsForWeb(Long sellerId, String keyword, LocalDate startAt, LocalDate endAt, Pageable pageable);
}
