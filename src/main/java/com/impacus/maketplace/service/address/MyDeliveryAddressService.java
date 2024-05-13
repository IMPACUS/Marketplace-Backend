package com.impacus.maketplace.service.address;


import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.address.AddressAddOrUpdateRequest;
import com.impacus.maketplace.dto.address.AddressResponse;
import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.address.MyDeliveryAddressRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyDeliveryAddressService {

    private final MyDeliveryAddressRepository myDeliveryAddressRepository;
    private final EntityManager em;

    @Transactional
    public AddressResponse addAddress(Long id, AddressAddOrUpdateRequest addressAddOrUpdateRequest) {
        MyDeliveryAddress address = myDeliveryAddressRepository.save(addressAddOrUpdateRequest.toEntity(getProxyUser(id)));
        return AddressResponse.of(address);
    }

    @Transactional
    public void deleteAddress(Long id, Long addressId) {
        MyDeliveryAddress address = myDeliveryAddressRepository.findByIdAndUser(addressId, getProxyUser(id))
            .orElseThrow(() -> new CustomException(CommonErrorType.INVALID_ID));
        myDeliveryAddressRepository.delete(address);
    }

    @Transactional
    public AddressResponse updateAddress(Long id, Long addressId, AddressAddOrUpdateRequest addressAddOrUpdateRequest) {
        MyDeliveryAddress address = myDeliveryAddressRepository.findByIdAndUser(addressId, getProxyUser(id))
            .orElseThrow(() -> new CustomException(CommonErrorType.INVALID_ID)); // 검증
        myDeliveryAddressRepository.save(addressAddOrUpdateRequest.toEntity(getProxyUser(id), addressId)); // 수정
        return AddressResponse.of(address);
    }

    public List<AddressResponse> getAddressList(Long id) {
        return AddressResponse.listOf(myDeliveryAddressRepository.findAllByUser(getProxyUser(id)));
    }

    public AddressResponse getAddress(Long id, Long addressId) {
        MyDeliveryAddress address = myDeliveryAddressRepository.findByIdAndUser(addressId, getProxyUser(id))
            .orElseThrow(() -> new CustomException(CommonErrorType.INVALID_ID));
        return AddressResponse.of(address);
    }

    private User getProxyUser(Long id) {
        return em.getReference(User.class, id);
    }
}
