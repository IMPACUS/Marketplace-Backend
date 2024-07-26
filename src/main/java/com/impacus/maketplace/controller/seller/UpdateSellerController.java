package com.impacus.maketplace.controller.seller;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.seller.request.*;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.auth.AuthService;
import com.impacus.maketplace.service.seller.SellerService;
import com.impacus.maketplace.service.seller.UpdateSellerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/seller")
public class UpdateSellerController {
    private final SellerService sellerService;
    private final UpdateSellerService updateSellerService;

    /**
     * 판매자 입점 요청 상태 변경 API
     *
     * @param request
     * @return
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_PRINCIPAL_ADMIN')or hasRole('ROLE_OWNER')")
    @PatchMapping("/entry/sellers/entry-status")
    public ApiResponseEntity<Boolean> changeEntryStatus(
            @Valid @RequestBody ChangeSellerEntryStatusDTO request) {
        Boolean result = sellerService.changeEntryStatus(request);
        return ApiResponseEntity.<Boolean>builder()
                .data(result)
                .build();
    }

    /**
     * 판매자 스토어 정보 변경
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/brand")
    public ApiResponseEntity<Boolean> updateBrandInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestPart(value = "seller") ChangeBrandInfoDTO dto,
            @RequestPart(value = "logoImage", required = false) MultipartFile logoImage
    ) {
        updateSellerService.updateBrandInformation(user.getId(), dto, logoImage);
        return ApiResponseEntity.<Boolean>builder()
                .message("판매자 스토어 정보 변경 성공")
                .data(true)
                .build();
    }

    /**
     * 판매자 담당자 정보 변경
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/manager")
    public ApiResponseEntity<Boolean> updateManagerInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestPart(value = "manager") ChangeSellerManagerInfoDTO dto,
            @RequestPart(value = "businessRegistrationImage", required = false) MultipartFile businessRegistrationImage,
            @RequestPart(value = "mailOrderBusinessReportImage", required = false) MultipartFile mailOrderBusinessReportImage
    ) {
        updateSellerService.updateManagerInformation(
                user.getId(), dto, businessRegistrationImage, mailOrderBusinessReportImage
        );
        return ApiResponseEntity
                .<Boolean>builder()
                .message("판매자 담당자 정보 변경 성공")
                .data(true)
                .build();
    }

    /**
     * 판매자 정산 정보 변경
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/adjustment")
    public ApiResponseEntity<Boolean> updateAdjustmentInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestPart(value = "adjustment") ChangeSellerAdjustmentInfoDTO dto,
            @RequestPart(value = "bankBookImage", required = false) MultipartFile bankBookImage
    ) {
        updateSellerService.updateAdjustmentInformation(
                user.getId(), dto, bankBookImage
        );
        return ApiResponseEntity
                .<Boolean>builder()
                .message("판매자 정산 정보 변경 성공")
                .data(true)
                .build();
    }

    /**
     * 판매자 로그인 정보 변경
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/login")
    public ApiResponseEntity<Boolean> updateLoginInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ChangeSellerLoginInfoDTO dto
    ) {
        updateSellerService.updateLoginInformation(user.getId(), dto);
        return ApiResponseEntity
                .<Boolean>builder()
                .message("판매자 로그인 정보 변경 성공")
                .data(true)
                .build();
    }

    /**
     * 판매자 택배사 정보 변경
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/delivery-company")
    public ApiResponseEntity<Boolean> updateDeliveryCompanyInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ChangeSellerDeliveryCompanyInfoDTO dto
    ) {
        updateSellerService.updateDeliveryCompanyInformation(user.getId(), dto);
        return ApiResponseEntity
                .<Boolean>builder()
                .message("판매자 택배사 정보 수정 성공")
                .data(true)
                .build();
    }

    /**
     * 판매자 배송지 정보 변경
     * @param dto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/delivery-address")
    public ApiResponseEntity<Boolean> updateDeliveryAddressInformation(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody ChangeSellerDeliveryAddressInfoDTO dto
    ) {
        updateSellerService.updateDeliveryAddressInformation(user.getId(), dto);
        return ApiResponseEntity
                .<Boolean>builder()
                .message("판매자 배송지 정보 수정 성공")
                .data(true)
                .build();
    }

    /**
     * 메인 판매자 배송지 정보 변경
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ROLE_APPROVED_SELLER')")
    @PatchMapping("/main-delivery-address")
    public ApiResponseEntity<Boolean> updateMainDeliveryAddress(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "delivery-address-id") Long sellerDeliveryAddressId
    ) {
        updateSellerService.updateMainDeliveryAddress(user.getId(), sellerDeliveryAddressId);
        return ApiResponseEntity
                .<Boolean>builder()
                .message("판매자 배송지 정보 수정 성공")
                .data(true)
                .build();
    }
}
