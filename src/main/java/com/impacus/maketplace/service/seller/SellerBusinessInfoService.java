package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.enumType.error.SellerErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.repository.seller.SellerBusinessInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerBusinessInfoService {
    private final SellerBusinessInfoRepository sellerBusinessInfoRepository;

    /**
     * SellerBusinessInfo 를 저장하는 함수
     *
     * @param sellerBusinessInfo
     */
    @Transactional
    public void saveSellerBusinessInfo(SellerBusinessInfo sellerBusinessInfo) {
        sellerBusinessInfoRepository.save(sellerBusinessInfo);
    }

    /**
     * sellerId로 SellerBusinessInfo를 조회하는 함수
     *
     * @param sellerId
     * @return
     */
    public SellerBusinessInfo findSellerBusinessInfoBySellerId(Long sellerId) {
        return sellerBusinessInfoRepository.findBySellerId(sellerId)
                .orElseThrow(() -> new CustomException(SellerErrorType.NOT_EXISTED_SELLER));
    }
}
