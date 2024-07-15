package com.impacus.maketplace.service.seller.delivery;

import com.impacus.maketplace.entity.seller.delivery.SellerDeliveryAddress;
import com.impacus.maketplace.repository.seller.delivery.SellerDeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerDeliveryAddressService {
    private final SellerDeliveryAddressRepository sellerDeliveryAddressRepository;

    /**
     * SellerDeliveryAddress 저장 함수
     *
     * @param sellerDeliveryAddress
     * @return
     */
    public SellerDeliveryAddress saveSellerDeliveryAddress(SellerDeliveryAddress sellerDeliveryAddress) {
        return sellerDeliveryAddressRepository.save(sellerDeliveryAddress);
    }
}
