package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListInfoDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.service.coupon.CouponAdminService;
import com.impacus.maketplace.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon")
public class CouponController {

    private final CouponAdminService couponAdminService;
    private final CouponService couponService;

    /**
     * ADMIN: 쿠폰 등록 API
     * @param couponIssuedDto
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
    "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/admin")
    public ApiResponseEntity<Boolean> registerCounponForAdmin(@Valid @RequestBody CouponIssueDTO couponIssuedDto) {
        Coupon savedCoupon = couponAdminService.addCoupon(couponIssuedDto);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 쿠폰 코드 중복 검사 API
     * @param couponCodeCheckDTO
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/coupon-code")
    public ApiResponseEntity<Boolean> checkDuplicateCode(@Valid  @RequestBody CouponCodeCheckDTO couponCodeCheckDTO) {
        couponService.duplicateCheckCode(couponCodeCheckDTO.getCode());

        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }

    /**
     * ADMIN: 쿠폰 수정 API
     * @param couponUpdateDTO 쿠폰 수정 정보
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PatchMapping("/admin")
    public ApiResponseEntity<Boolean> updateCoupon(@Valid @RequestBody CouponUpdateDTO couponUpdateDTO) {
        Coupon updateCoupon = couponAdminService.updateCoupon(couponUpdateDTO);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 쿠폰 상세 조회 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ApiResponseEntity<CouponDetailDTO> getCouponDetail(@RequestParam(value = "coupon-id") Long id) {

        CouponDetailDTO couponDetailDTO = couponAdminService.getCoupon(id);

        return ApiResponseEntity
                .<CouponDetailDTO>builder()
                .data(couponDetailDTO)
                .build();
    }

    /**
     * ADMIN: 쿠폰 리스트 상태 변경
     * @param couponIdList couponId 리스트
     * @param changeCouponStatus ISSUING, ISSUED, STOP
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PatchMapping("/status")
    public ApiResponseEntity<Boolean> changeCouponStatus(@RequestParam(value = "coupon-id") List<Long> couponIdList,
                                                         @RequestParam(value = "status") CouponStatusType changeCouponStatus) {

        couponAdminService.changeStatus(couponIdList, changeCouponStatus);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 쿠폰 다중 삭제 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteCoupons(@RequestParam(name = "coupon-id") List<Long> couponIdList) {

        // 1. 쿠폰 삭제하기
        couponAdminService.deleteCoupon(couponIdList);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }


    /** [개발중]
     * ADMIN: 쿠폰 목록 조회 Pagination API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/list")
    public ApiResponseEntity<Page<CouponListInfoDTO>> getCouponList(@RequestParam(name = "keyword", required = false) String keyword,
                                             @RequestParam(name = "status", required = false) String status,
                                             @PageableDefault(sort = "modifyAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CouponListInfoDTO> couponListInfoDTOList = couponAdminService.getCouponListInfoList(keyword, status, pageable);

        return ApiResponseEntity
                .<Page<CouponListInfoDTO>>builder()
                .data(couponListInfoDTOList)
                .build();
    }
}
