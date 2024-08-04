package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.*;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

import java.time.LocalDate;
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
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/coupon-code")
    public ApiResponseEntity<Boolean> checkDuplicateCode(@Valid @RequestBody CouponCodeDTO couponCodeDTO) {
        couponAdminService.duplicateCheckCode(couponCodeDTO.getCouponCode());

        return ApiResponseEntity
                .<Boolean>builder()
                .data(true)
                .build();
    }

    /**
     * ADMIN: 쿠폰 수정 API
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
     *
     * @param changeCouponStatusDTO couponIdList, {ISSUING, ISSUED, STOP}
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PatchMapping("/admin/status")
    public ApiResponseEntity<Boolean> changeCouponStatus(@Valid @RequestBody ChangeCouponStatusDTO changeCouponStatusDTO) {

        couponAdminService.changeStatus(changeCouponStatusDTO.getCouponIdList(), changeCouponStatusDTO.getStatus());

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 쿠폰 다중 삭제 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @DeleteMapping("/admin")
    public ApiResponseEntity<Boolean> deleteCoupons(@Valid @RequestBody CouponIdListDTO couponIdListDTO) {

        // 1. 쿠폰 삭제하기
        couponAdminService.deleteCoupon(couponIdListDTO.getCouponIdList());

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
    @GetMapping("/admin/issue-coupon/info-list")
    public ApiResponseEntity<List<IssueCouponInfoDTO>> getIssueCouponInfoList() {

        List<IssueCouponInfoDTO> issueCouponInfoList = couponAdminService.getIssueCouponInfoList();

        return ApiResponseEntity
                .<List<IssueCouponInfoDTO>>builder()
                .data(issueCouponInfoList)
                .build();
    }


    /**
     * [개발 미완성]: level_point_master 엔티티 확정 후 레벨별 지급 가능(회원 가져오는 기능 필요)
     * ADMIN: 모든 회원 쿠폰 지급 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/admin/issue-coupon/all-user")
    public ApiResponseEntity<Boolean> issueCouponAllUser(@Valid @RequestBody IssueCouponAllUserDTO issueCouponAllUserDTO) {

        couponAdminService.issueCouponAllUser(issueCouponAllUserDTO);

        return null;
    }

    /**
     * ADMIN: 특정 회원 쿠폰 지급 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/admin/issue-coupon")
    public ApiResponseEntity<Boolean> issueCouponTargetUser(@Valid @RequestBody IssueCouponTargetUserDTO issueCouponTargetUserDTO) {

        couponAdminService.issueCouponTargetUser(issueCouponTargetUserDTO);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 쿠폰 지급 내역 Pagination API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/issue-coupon/history-list")
    public ApiResponseEntity<Page<IssueCouponHIstoryDTO>> getIssueCouponHistoryList(@RequestParam(name = "name", required = false) String name,
                                                                                    @RequestParam(name = "status", required = false) UserCouponStatus userCouponStatus,
                                                                                    @RequestParam(name = "start-at", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
                                                                                    @RequestParam(name = "end-at", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
                                                                                    @PageableDefault(sort = "issueDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<IssueCouponHIstoryDTO> issueCouponHistoryList = couponAdminService.getIssueCouponHistoryList(name, userCouponStatus, startAt, endAt, pageable);

        return ApiResponseEntity
                .<Page<IssueCouponHIstoryDTO>>builder()
                .data(issueCouponHistoryList)
                .build();
    }

    /**
     * APP: 쿠폰함 - 보유 쿠폰 List API
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/coupon-box/coupon-list")
    public ApiResponseEntity<List<UserCouponOverviewDTO>> getUserCouponOverviewList(@AuthenticationPrincipal CustomUserDetails user) {

        List<UserCouponOverviewDTO> userCouponOverviewList = couponService.getUserCouponOverviewList(user.getId());

        return ApiResponseEntity
                .<List<UserCouponOverviewDTO>>builder()
                .data(userCouponOverviewList)
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

        UserCouponOverviewDTO userCouponOverviewDTO = couponService.registerUserCoupon(user.getId(), registerUserCouponDTO.getCouponCode());

        return ApiResponseEntity
                .<UserCouponOverviewDTO>builder()
                .data(userCouponOverviewDTO)
                .build();
    }

    /**
     * APP: 쿠폰함 - 쿠폰 다운로드
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/coupon-box/coupon/download")
    public ApiResponseEntity<UserCouponDownloadDTO> downloadUserCoupon(@AuthenticationPrincipal CustomUserDetails user,
                                                         @Valid @RequestBody UserCouponIdDTO couponIdDTO) {

        UserCouponDownloadDTO userCouponDownloadDTO = couponService.downloadUserCoupon(user.getId(), couponIdDTO.getUserCouponId());

        return ApiResponseEntity
                .<UserCouponDownloadDTO>builder()
                .data(userCouponDownloadDTO)
                .build();
    }

    /**
     * APP: 브랜드 쿠폰 받기 - 쿠폰 List API
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @GetMapping("/brand-coupon/coupon-list")
    public ApiResponseEntity<List<UserCouponOverviewDTO>> getBrandCouponList(@AuthenticationPrincipal CustomUserDetails user,
                                                                             @RequestParam(name = "brand-name") String brandName,
                                                                             @RequestParam(name = "eco-product") Boolean isEcoProduct) {

        couponService.getBrandCouponList(user.getId(), brandName);
        return null;
    }

    /**
     * APP: 브랜드 쿠폰 받기 - 쿠폰 다운 받기 API
     */
    @PreAuthorize("hasRole('ROLE_CERTIFIED_USER')")
    @PostMapping("/brand-coupon/download")
    public ApiResponseEntity<UserCouponDownloadDTO> issueAndDownloadCoupon(@AuthenticationPrincipal CustomUserDetails user,
                                                                        @Valid @RequestBody CouponIdDTO couponDownloadDTO) {

        UserCouponDownloadDTO userCouponDownloadDTO = couponService.issueAndDownloadCoupon(user.getId(), couponDownloadDTO.getCouponId());
        return ApiResponseEntity
                .<UserCouponDownloadDTO>builder()
                .data(userCouponDownloadDTO)
                .build();
    }

}
