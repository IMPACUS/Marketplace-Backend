package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.dto.seller.request.ChangeBrandInfoDTO;

public interface SellerWriteCustomRepository {
    void updateBrandInformationByUserId(Long userId, Long sellerId, ChangeBrandInfoDTO dto, boolean isExistedBrand);
}
