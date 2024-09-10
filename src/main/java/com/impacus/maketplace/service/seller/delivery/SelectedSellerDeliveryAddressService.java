package com.impacus.maketplace.service.seller.delivery;

import com.impacus.maketplace.entity.seller.delivery.SelectedSellerDeliveryAddress;
import com.impacus.maketplace.repository.seller.delivery.SelectedSellerDeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SelectedSellerDeliveryAddressService {
    private final SelectedSellerDeliveryAddressRepository selectedSellerDeliveryAddressRepository;

    /**
     * SelectedSellerDeliveryAddress 저장 함수
     * @param address
     */
    @Transactional
    public void saveSelectedSellerDeliveryAddress(SelectedSellerDeliveryAddress address) {
        selectedSellerDeliveryAddressRepository.save(address);
    }
}
