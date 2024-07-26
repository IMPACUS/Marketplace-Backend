package com.impacus.maketplace.repository.seller.querydsl;

import com.impacus.maketplace.dto.seller.request.*;

public interface UpdateSellerCustomRepository {
    void updateBrandInformationByUserId(Long userId, Long sellerId, ChangeBrandInfoDTO dto, boolean isExistedBrand);

    void updateManagerInformationBySellerId(Long sellerId, ChangeSellerManagerInfoDTO dto);

    void updateAdjustmentInformationBySellerId(Long sellerId, ChangeSellerAdjustmentInfoDTO dto);

    void updateLoginInformationByUserId(Long userId, ChangeSellerLoginInfoDTO dto, String encodedPassword);

    void updateDeliveryCompanyInformationBySellerId(Long sellerId, ChangeSellerDeliveryCompanyInfoDTO dto);

    Long updateDeliveryAddressInformationBySellerIdAndId(Long sellerId, ChangeSellerDeliveryAddressInfoDTO dto);
}
