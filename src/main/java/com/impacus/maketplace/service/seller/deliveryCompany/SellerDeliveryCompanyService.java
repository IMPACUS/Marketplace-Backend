package com.impacus.maketplace.service.seller.deliveryCompany;

import com.impacus.maketplace.common.enumType.error.SellerErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.seller.deliveryCompany.SellerDeliveryCompany;
import com.impacus.maketplace.repository.seller.deliveryCompany.SellerDeliveryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerDeliveryCompanyService {
    private final SellerDeliveryCompanyRepository sellerDeliveryCompanyRepository;

    /**
     * SellerDeliveryCompany 저장하는 함수
     *
     * @param company
     */
    @Transactional
    public void saveSellerDeliveryCompany(SellerDeliveryCompany company) {
        sellerDeliveryCompanyRepository.save(company);
    }


    /**
     * sellerId로 SellerDeliveryCompany 조회하는 함수
     *
     * @param sellerId
     */
    public SellerDeliveryCompany findSellerDeliveryCompanyBySellerId(Long sellerId) {
        return sellerDeliveryCompanyRepository.findBySellerId(sellerId)
                .orElseThrow(() -> new CustomException(SellerErrorType.NOT_EXISTED_SELLER));
    }
}
