package com.impacus.maketplace.controller.address;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.address.request.DeleteAddressDTO;
import com.impacus.maketplace.dto.address.request.UpsertAddressDTO;
import com.impacus.maketplace.dto.address.response.AddressInfoDTO;
import com.impacus.maketplace.service.address.MyDeliveryAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/address")
public class AddressController {

    private final MyDeliveryAddressService myDeliveryAddressService;

    /**
     * 주소 목록 조회 API
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<List<AddressInfoDTO>> getAddressInfoList(@AuthenticationPrincipal CustomUserDetails user) {
        List<AddressInfoDTO> response = myDeliveryAddressService.getAddressInfoList(user.getId());

        return ApiResponseEntity
                .<List<AddressInfoDTO>>builder()
                .data(response)
                .build();
    }

    /**
     * id를 통해 주소 조회
     * @param addressId 특정 주소의 id
     */
    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<AddressInfoDTO> getAddressInfo(@AuthenticationPrincipal CustomUserDetails user,
                                                            @RequestParam(name = "address-id") Long addressId) {

        AddressInfoDTO response = myDeliveryAddressService.getAddressInfo(user.getId(), addressId);

        return ApiResponseEntity
                .<AddressInfoDTO>builder()
                .data(response)
                .build();
    }

    /**
     * id를 통해서 주소를 등록 혹은 업데이트
     * @param upsertAddressDTO 등록 혹은 업데이트 주소지 정보
     */
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<AddressInfoDTO> upsertAddress(@AuthenticationPrincipal CustomUserDetails user,
                                                             @RequestBody @Valid UpsertAddressDTO upsertAddressDTO) {

        AddressInfoDTO response = myDeliveryAddressService.upsertAddress(user.getId(), upsertAddressDTO);

        return ApiResponseEntity
                .<AddressInfoDTO>builder()
                .data(response)
                .build();
    }

    /**
     * id를 통해서 주소지 삭제
     * @param deleteAddressDTO 삭제할 주소지 id
     */
    @DeleteMapping("")
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    public ApiResponseEntity<Boolean> deleteAddress(@AuthenticationPrincipal CustomUserDetails user,
                                                    @RequestBody @Valid DeleteAddressDTO deleteAddressDTO) {

        myDeliveryAddressService.deleteAddress(user.getId(), deleteAddressDTO.getId());

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

}
