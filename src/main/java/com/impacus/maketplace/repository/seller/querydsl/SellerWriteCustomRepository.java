package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.dto.seller.request.ChangeBrandInfoDTO;
import com.impacus.maketplace.dto.seller.request.ChangeSellerManagerInfoDTO;

public interface SellerWriteCustomRepository {
    void updateBrandInformationByUserId(Long userId, Long sellerId, ChangeBrandInfoDTO dto, boolean isExistedBrand);

    void updateManagerInformationByUserId(Long sellerId, ChangeSellerManagerInfoDTO dto);
}
