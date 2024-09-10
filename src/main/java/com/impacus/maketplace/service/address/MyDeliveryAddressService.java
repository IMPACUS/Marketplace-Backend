package com.impacus.maketplace.service.address;


import com.impacus.maketplace.common.enumType.error.AddressErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.address.request.UpsertAddressDTO;
import com.impacus.maketplace.dto.address.response.AddressInfoDTO;
import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import com.impacus.maketplace.repository.address.MyDeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyDeliveryAddressService {

    private final MyDeliveryAddressRepository myDeliveryAddressRepository;

    /**
     * 사용자의 MyAddress 조회
     *
     * @param userId 사용자 아이디
     */
    public List<AddressInfoDTO> getAddressInfoList(Long userId) {
        List<MyDeliveryAddress> myDeliveryAddresses = myDeliveryAddressRepository.findAllByUserId(userId);

        return myDeliveryAddresses.stream().map(AddressInfoDTO::new).toList();
    }

    /**
     * 사용자의 주소지 id를 가진 MyAddress 조회
     *
     * @param userId    사용자 아이디
     * @param addressId 주소 id
     */
    public AddressInfoDTO getAddressInfo(Long userId, Long addressId) {
        // id로 조회
        MyDeliveryAddress myDeliveryAddress = myDeliveryAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new CustomException(AddressErrorType.NOT_FOUND_MY_ADDRESS_BY_ID));

        return new AddressInfoDTO(myDeliveryAddress);
    }

    /**
     * 사용자의 주소지를 등록하거나 특정 id를 가진 주소지를 업데이트
     *
     * @param userId             사용자 아이디
     * @param registerAddressDTO 등록 혹은 업데이트 하려는 주소지 정보
     */
    @Transactional
    public AddressInfoDTO upsertAddress(Long userId, UpsertAddressDTO registerAddressDTO) {

        // 1. id가 있는 경우 수정으로 간주
        if (registerAddressDTO.getId() != null) {
            // 2. id를 통해서 MyDeliveryAddress가 있는지 확인
            MyDeliveryAddress myDeliveryAddress = myDeliveryAddressRepository.findByIdAndUserId(registerAddressDTO.getId(), userId)
                    .orElseThrow(() -> new CustomException(AddressErrorType.NOT_FOUND_MY_ADDRESS_BY_ID));

            // 3. 업데이트
            updateAddress(myDeliveryAddress, registerAddressDTO);

            return new AddressInfoDTO(myDeliveryAddress);
        }

        // 4. 없을 경우 유효성 검증 (최대 수, 명칭 중복)
        validateInsertAddress(userId, registerAddressDTO.getName());
        
        // 5. 등록
        MyDeliveryAddress myDeliveryAddress = createAndSaveAddress(userId, registerAddressDTO);
        return new AddressInfoDTO(myDeliveryAddress);
    }
    private void validateInsertAddress(Long userId, String name) {
        // 1. 저장된 주소지 수 검증 (최대 3개)
        Long count = myDeliveryAddressRepository.countByUserId(userId);

        if (count >= 3) {
            throw new CustomException(AddressErrorType.MAX_REGIST_ADDRESS);
        }

        // 2. 해당 유저에게 등록된 주소지 명칭 중복 검증
        Boolean nameExist = myDeliveryAddressRepository.existsByUserIdAndName(userId, name);

        if (nameExist) {
            throw new CustomException(AddressErrorType.DUPLICATE_NAME_ADDRESS);
        }
    }
    private void updateAddress(MyDeliveryAddress myDeliveryAddress, UpsertAddressDTO registerAddressDTO) {
        myDeliveryAddress.updateAddress(
                registerAddressDTO.getReceiver(),
                registerAddressDTO.getPostalCode(),
                registerAddressDTO.getAddress(),
                registerAddressDTO.getDetailAddress(),
                registerAddressDTO.getConnectNumber(),
                registerAddressDTO.getMemo());
    }
    private MyDeliveryAddress createAndSaveAddress(Long userId, UpsertAddressDTO dto) {
        MyDeliveryAddress newAddress =
                MyDeliveryAddress.builder()
                        .userId(userId)
                        .name(dto.getName())
                        .receiver(dto.getReceiver())
                        .postalCode(dto.getPostalCode())
                        .address(dto.getAddress())
                        .detailAddress(dto.getDetailAddress())
                        .connectNumber(dto.getConnectNumber())
                        .memo(dto.getMemo())
                        .build();

        return myDeliveryAddressRepository.save(newAddress);
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {

        // 1. id를 통해 주소지 가져오기
        MyDeliveryAddress myDeliveryAddress = myDeliveryAddressRepository.findByIdAndUserId(addressId, userId)
                .orElseThrow(() -> new CustomException(AddressErrorType.NOT_FOUND_MY_ADDRESS_BY_ID));

        // 2. 삭제
        myDeliveryAddressRepository.delete(myDeliveryAddress);
    }
}
