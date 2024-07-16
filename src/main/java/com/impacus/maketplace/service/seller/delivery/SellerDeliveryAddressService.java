package com.impacus.maketplace.service.seller.delivery;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.impacus.maketplace.entity.seller.delivery.SellerDeliveryAddress;
import com.impacus.maketplace.repository.seller.delivery.SellerDeliveryAddressRepository;

import lombok.RequiredArgsConstructor;

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

    /**
     * id가 sellerDeliveryAddressId인, sellerId가 등록한 SellerDeliveryAddress가 존재하는지 확인하는 함수
     * @param sellerId
     * @param sellerDeliveryAddressId
     * @return
     */
    public boolean existsSellerDeliveryAddressBySellerIdAndId(Long sellerId, Long sellerDeliveryAddressId) {
        return sellerDeliveryAddressRepository.existsBySellerIdAndId(sellerId, sellerDeliveryAddressId);
    }
}
