package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.dto.seller.request.ChangeBrandInfoDTO;
import com.impacus.maketplace.dto.seller.request.ChangeSellerAdjustmentInfoDTO;
import com.impacus.maketplace.dto.seller.request.ChangeSellerLoginInfoDTO;
import com.impacus.maketplace.dto.seller.request.ChangeSellerManagerInfoDTO;

public interface SellerWriteCustomRepository {
    void updateBrandInformationByUserId(Long userId, Long sellerId, ChangeBrandInfoDTO dto, boolean isExistedBrand);

    void updateManagerInformationBySellerId(Long sellerId, ChangeSellerManagerInfoDTO dto);

    void updateAdjustmentInformationBySellerId(Long sellerId, ChangeSellerAdjustmentInfoDTO dto);

    void updateLoginInformationByUserId(Long userId, ChangeSellerLoginInfoDTO dto, String encodedPassword);
}
