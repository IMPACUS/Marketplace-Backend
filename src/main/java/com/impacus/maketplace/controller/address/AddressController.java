package com.impacus.maketplace.controller.address;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.address.AddressAddOrUpdateRequest;
import com.impacus.maketplace.dto.address.AddressResponse;
import com.impacus.maketplace.service.address.MyDeliveryAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
     * @return
     */
    @GetMapping("/list")
    public ApiResponseEntity<List<AddressResponse>> getAddressList(@AuthenticationPrincipal CustomUserDetails user) {
        List<AddressResponse> response = myDeliveryAddressService.getAddressList(user.getId());
        return ApiResponseEntity.<List<AddressResponse>>builder()
                .data(response)
                .build();
    }

    /**
     * 주소 상세 조회 API
     */
    @GetMapping
    public ApiResponseEntity<AddressResponse> getAddress(@AuthenticationPrincipal CustomUserDetails user,
                                                        @RequestParam(name = "address-id") Long addressId) {
        AddressResponse response = myDeliveryAddressService.getAddress(user.getId(), addressId);
        return ApiResponseEntity.<AddressResponse>builder()
                .data(response)
                .build();
    }

    /**
     * 주소 등록 API
     * @param addressAddOrUpdateRequest
     * @return
     */
//    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ApiResponseEntity<AddressResponse> addAddress(@AuthenticationPrincipal CustomUserDetails user,
                                                         @RequestBody @Valid AddressAddOrUpdateRequest addressAddOrUpdateRequest) {
        AddressResponse response = myDeliveryAddressService.addAddress(user.getId(), addressAddOrUpdateRequest);
        return ApiResponseEntity.<AddressResponse>builder()
                .data(response)
                .build();
    }

    /**
     * 주소 삭제 API
     * @param addressId
     * @return
     */
    @DeleteMapping
    public ApiResponseEntity<Boolean> deleteAddress(@AuthenticationPrincipal CustomUserDetails user,
                                                    @RequestParam(name = "address-id") Long addressId) {
        myDeliveryAddressService.deleteAddress(user.getId(), addressId);
        return ApiResponseEntity.simpleResult(HttpStatus.NO_CONTENT);
    }

    /**
     * 주소 수정 API
     * @param addressId
     * @param addressAddOrUpdateRequest
     * @return
     */
    @PutMapping
    public ApiResponseEntity<AddressResponse> updateAddress(@AuthenticationPrincipal CustomUserDetails user,
                                                            @RequestParam(name = "address-id") Long addressId,
                                                            @RequestBody @Valid AddressAddOrUpdateRequest addressAddOrUpdateRequest) {
        AddressResponse response = myDeliveryAddressService.updateAddress(user.getId(), addressId, addressAddOrUpdateRequest);
        return ApiResponseEntity.<AddressResponse>builder()
                .data(response)
                .build();
    }



}
