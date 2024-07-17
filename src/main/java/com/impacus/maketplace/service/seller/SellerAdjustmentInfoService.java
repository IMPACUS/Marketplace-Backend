package com.impacus.maketplace.service.seller;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.repository.seller.SellerAdjustmentInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerAdjustmentInfoService {
    private final SellerAdjustmentInfoRepository sellerAdjustmentInfoRepository;

    /**
     * SellerAdjustment 를 저장하는 함수
     *
     * @param adjustmentInfo
     */
    @Transactional
    public void saveSellerAdjustmentInfo(SellerAdjustmentInfo adjustmentInfo) {
        sellerAdjustmentInfoRepository.save(adjustmentInfo);
    }

    /**
     * sellerId로 SellerAdjustmentInfo 를 조회하는 함수
     *
     * @param sellerId
     * @return
     */
    public SellerAdjustmentInfo findSellerAdjustmentInfoBySellerId(Long sellerId) {
        return sellerAdjustmentInfoRepository.findBySellerId(sellerId)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_SELLER));
    }
}
