package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.IssueCouponInfoDTO;
import com.impacus.maketplace.dto.coupon.response.UserCouponOverviewDTO;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListInfoDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.service.coupon.CouponAdminService;
import com.impacus.maketplace.service.coupon.CouponUserService;
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

    // 용어 정리
    // ADMIN이 새롭게 쿠폰 등록: Register
    // ADMIN -> USER과 같은 USER에게 지급: Issue
    // USER가 쿠폰을 사용: Redeem

    private final CouponAdminService couponAdminService;
    private final CouponUserService couponService;

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
     * @param changeCouponStatusDTO ISSUING, ISSUED, STOP
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PatchMapping("/status")
    public ApiResponseEntity<Boolean> changeCouponStatus(@RequestParam(value = "coupon-id") List<Long> couponIdList,
                                                         @Valid @RequestBody ChangeCouponStatusDTO changeCouponStatusDTO) {

        couponAdminService.changeStatus(couponIdList, changeCouponStatusDTO.getStatus());

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


    /**
     * ADMIN: 쿠폰 목록 조회 Pagination API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/list")
    public ApiResponseEntity<Page<CouponListInfoDTO>> getCouponList(@RequestParam(name = "name", required = false) String name,
                                                                     @RequestParam(name = "status", required = false) CouponStatusType couponStatus,
                                                                     @PageableDefault(sort = "modifyAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CouponListInfoDTO> couponListInfoDTOList = couponAdminService.getCouponListInfoList(name, couponStatus, pageable);

        return ApiResponseEntity
                .<Page<CouponListInfoDTO>>builder()
                .data(couponListInfoDTOList)
                .build();
    }

    /**
     * ADMIN: 쿠폰 지급::쿠폰명 선택 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @GetMapping("/admin/pay-coupon-info/list")
    public ApiResponseEntity<List<IssueCouponInfoDTO>> getIssueCouponInfoList() {

        List<IssueCouponInfoDTO> payCouponInfoList = couponAdminService.getIssueCouponInfoList();

        return ApiResponseEntity
                .<List<IssueCouponInfoDTO>>builder()
                .data(payCouponInfoList)
                .build();
    }


    /**
     * [개발 미완성]: level_point_master 엔티티 확정 후 레벨별 지급 가능(회원 가져오는 기능 필요)
     * ADMIN: 모든 회원 쿠폰 지급 API
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/admin/pay-coupon/all-user")
    public ApiResponseEntity<Boolean> issueCouponAllUser(@Valid @RequestBody IssueCouponAllUserDTO payCouponAllUserDTO) {

        couponAdminService.issueCouponAllUser(payCouponAllUserDTO);

        return null;
    }

    /**
     * [개발 준비]: 쿠폰 수정 불가 정책 확정시 개발 진행
     * ADMIN: 특정 회원 쿠폰 지급 API
     * @return
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping()
    public ApiResponseEntity<Boolean> issueCouponTargetUser(@Valid @RequestBody IssueCouponTargetUserDTO payCouponTargetUserDTO) {

        couponAdminService.issueCouponTargetUser(payCouponTargetUserDTO);

        return null;
    }

    /**
     * [개발 준비]: 지급 상태에 대한 정리가 끝나면 개발 진행
     * ADMIN: 쿠폰 지급 내역 Pagination API
     */
    public ApiResponseEntity<Void> getIssueCouponHistory() {
        return null;
    }

    /**
     * [개발 준비]: 모든 회원 쿠폰 지급 기능 구현시 개발 진행
     * APP: 보유 쿠폰 List API
     * @param user
     * @return
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/coupon-box/coupons")
    public ApiResponseEntity<List<UserCouponOverviewDTO>> getUserCouponOverviewList(@AuthenticationPrincipal CustomUserDetails user) {

        couponService.getUserCouponOverviewList(user);

        return null;
    }

    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/coupon-box/coupon")
    public ApiResponseEntity<List<UserCouponOverviewDTO>> registUserCoupon(@AuthenticationPrincipal CustomUserDetails user) {

        return null;
    }

}
