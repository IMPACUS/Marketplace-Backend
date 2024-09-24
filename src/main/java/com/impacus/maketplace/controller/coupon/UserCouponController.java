package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.CouponCodeDTO;
import com.impacus.maketplace.dto.coupon.request.CouponIdDTO;
import com.impacus.maketplace.dto.coupon.request.ProductQuantityDTO;
import com.impacus.maketplace.dto.coupon.request.UserCouponIdDTO;
import com.impacus.maketplace.dto.coupon.response.*;
import com.impacus.maketplace.service.coupon.CouponUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon")
public class UserCouponController {
    // 용어 정리
    // ADMIN이 새롭게 쿠폰 등록: Register
    // ADMIN -> USER과 같은 USER에게 지급: Issue
    // USER가 쿠폰을 사용: Redeem

    private final CouponUserService couponService;


    /**
     * APP: 쿠폰함 - 보유 쿠폰 List API
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/coupon-box/coupon-list")
    public ApiResponseEntity<List<UserCouponOverviewDTO>> getUserCouponOverviewList(@AuthenticationPrincipal CustomUserDetails user) {

        List<UserCouponOverviewDTO> response = couponService.getUserCouponOverviewList(user.getId());

        return ApiResponseEntity
                .<List<UserCouponOverviewDTO>>builder()
                .data(response)
                .build();
    }

    /**
     * POSTMANE API 수정하기
     * APP: 쿠폰함 - 쿠폰 등록하기
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/coupon-box/coupon")
    public ApiResponseEntity<UserCouponOverviewDTO> registUserCoupon(@AuthenticationPrincipal CustomUserDetails user,
                                                                     @Valid @RequestBody CouponCodeDTO registerUserCouponDTO) {

        UserCouponOverviewDTO response = couponService.registerUserCoupon(user.getId(), registerUserCouponDTO.getCouponCode());

        return ApiResponseEntity
                .<UserCouponOverviewDTO>builder()
                .data(response)
                .build();
    }

    /**
     * APP: 쿠폰함 - 쿠폰 다운로드
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/coupon-box/coupon/download")
    public ApiResponseEntity<UserCouponDownloadDTO> downloadUserCoupon(@AuthenticationPrincipal CustomUserDetails user,
                                                                       @Valid @RequestBody UserCouponIdDTO couponIdDTO) {

        UserCouponDownloadDTO response = couponService.downloadUserCoupon(user.getId(), couponIdDTO.getUserCouponId());

        return ApiResponseEntity
                .<UserCouponDownloadDTO>builder()
                .data(response)
                .build();
    }

    /**
     * APP: 브랜드 쿠폰 받기 - 쿠폰 List API
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/brand-coupon/coupon-list")
    public ApiResponseEntity<List<BrandCouponOverviewDTO>> getBrandCouponList(@AuthenticationPrincipal CustomUserDetails user,
                                                                              @RequestParam(name = "brand-name") String brandName,
                                                                              @RequestParam(name = "eco-product") Boolean isEcoProduct) {

        List<BrandCouponOverviewDTO> response = couponService.getBrandCouponList(user.getId(), brandName, isEcoProduct);

        return ApiResponseEntity
                .<List<BrandCouponOverviewDTO>>builder()
                .data(response)
                .build();
    }

    /**
     * APP: 브랜드 쿠폰 받기 - 쿠폰 다운 받기 API
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/brand-coupon/download")
    public ApiResponseEntity<UserCouponDownloadDTO> issueAndDownloadCoupon(@AuthenticationPrincipal CustomUserDetails user,
                                                                           @Valid @RequestBody CouponIdDTO couponDownloadDTO) {

        UserCouponDownloadDTO response = couponService.issueAndDownloadCoupon(user.getId(), couponDownloadDTO.getCouponId());
        return ApiResponseEntity
                .<UserCouponDownloadDTO>builder()
                .data(response)
                .build();
    }

    /**
     * 결제 페이지: 사용 가능한 쿠폰 List 조회
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/available-for-checkout")
    public ApiResponseEntity<AvailableCouponsForCheckoutDTO> getAvailableCouponsForCheckout(@AuthenticationPrincipal CustomUserDetails user,
                                                                                                  @RequestBody List<ProductQuantityDTO> productQuantityDTOList) {
        AvailableCouponsForCheckoutDTO response = couponService.findAvailableCouponsForCheckout(user.getId(), productQuantityDTOList);

        return ApiResponseEntity
                .<AvailableCouponsForCheckoutDTO>builder()
                .data(response)
                .build();
    }

}
