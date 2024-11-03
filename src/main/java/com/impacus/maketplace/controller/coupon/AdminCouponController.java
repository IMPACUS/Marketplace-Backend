package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.enumType.coupon.CouponStatusType;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.common.response.FileGenerationStatusIdDTO;
import com.impacus.maketplace.dto.coupon.api.CouponNameDTO;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListInfoDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponHistoryDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponInfoDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.service.coupon.CouponAdminService;
import com.impacus.maketplace.service.coupon.CouponApiServiceImpl;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon/admin")
public class AdminCouponController {

    // 용어 정리
    // ADMIN이 새롭게 쿠폰 등록: Register
    // ADMIN -> USER과 같은 USER에게 지급: Issue
    // USER가 쿠폰을 사용: Redeem

    private final CouponAdminService couponAdminService;
    private final CouponApiServiceImpl couponApiService;

    /**
     * ADMIN: 쿠폰 등록 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("")
    public ApiResponseEntity<Boolean> registerCounponForAdmin(@Valid @RequestBody CouponIssueDTO couponIssuedDto) {
        Coupon savedCoupon = couponAdminService.registerCoupon(couponIssuedDto);

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
    @PatchMapping("")
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
    @GetMapping("")
    public ApiResponseEntity<CouponDetailDTO> getCouponDetail(@RequestParam(value = "coupon-id") Long id) {

        CouponDetailDTO response = couponAdminService.getCoupon(id);

        return ApiResponseEntity
                .<CouponDetailDTO>builder()
                .data(response)
                .build();
    }

    /**
     * ADMIN: 쿠폰 리스트 상태 변경
     *
     * @param changeCouponStatusDTO couponIdList, {ISSUING, ISSUED, STOP}
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PatchMapping("/status")
    public ApiResponseEntity<Boolean> changeCouponStatus(@Valid @RequestBody ChangeCouponStatusDTO changeCouponStatusDTO) {

        couponAdminService.changeStatus(changeCouponStatusDTO.getCouponIdList(), changeCouponStatusDTO.getStatus());

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 쿠폰 다중 삭제 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @DeleteMapping("")
    public ApiResponseEntity<Boolean> deleteCoupons(@RequestParam(value = "coupon-ids") List<Long> couponIds) {

        // 1. 쿠폰 삭제하기
        couponAdminService.deleteCoupon(couponIds);

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }


    /**
     * ADMIN: 쿠폰 목록 조회 Pagination API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public ApiResponseEntity<Page<CouponListInfoDTO>> getCouponList(@RequestParam(name = "name", required = false) String name,
                                                                    @RequestParam(name = "status", required = false) CouponStatusType couponStatus,
                                                                    @PageableDefault(sort = "modifyAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<CouponListInfoDTO> response = couponAdminService.getCouponListInfoList(name, couponStatus, pageable);

        return ApiResponseEntity
                .<Page<CouponListInfoDTO>>builder()
                .data(response)
                .build();
    }

    /**
     * ADMIN: 쿠폰 지급::쿠폰명 선택 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @GetMapping("/issue-coupon/info-list")
    public ApiResponseEntity<List<IssueCouponInfoDTO>> getIssueCouponInfoList() {

        List<IssueCouponInfoDTO> response = couponAdminService.getIssueCouponInfoList();

        return ApiResponseEntity
                .<List<IssueCouponInfoDTO>>builder()
                .data(response)
                .build();
    }


    /**
     * ADMIN: 모든 회원 쿠폰 지급 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/issue-coupon/all-user")
    public ApiResponseEntity<Boolean> issueCouponAllUser(@Valid @RequestBody IssueCouponAllUserDTO issueCouponAllUserDTO) {

        couponAdminService.issueCouponAllUser(issueCouponAllUserDTO.getCouponId(), issueCouponAllUserDTO.getUserLevel());

        return ApiResponseEntity.simpleResult(HttpStatus.OK);
    }

    /**
     * ADMIN: 특정 회원 쿠폰 지급 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN')")
    @PostMapping("/issue-coupon")
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
    @GetMapping("/issue-coupon/history-list")
    public ApiResponseEntity<Page<IssueCouponHistoryDTO>> getIssueCouponHistoryList(@RequestParam(name = "name", required = false) String name,
                                                                                    @RequestParam(name = "status", required = false) UserCouponStatus userCouponStatus,
                                                                                     @RequestParam(name = "start-at", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
                                                                                    @RequestParam(name = "end-at", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
                                                                                    @PageableDefault(sort = "issueDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<IssueCouponHistoryDTO> response = couponAdminService.getIssueCouponHistoryList(name, userCouponStatus, startAt, endAt, pageable);

        return ApiResponseEntity
                .<Page<IssueCouponHistoryDTO>>builder()
                .data(response)
                .build();
    }

    /**
     * ADMIN: 회원 포인트&쿠폰 지급::쿠폰명 조회 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @GetMapping("/names")
    public ApiResponseEntity<List<CouponNameDTO>> getCouponNames() {
        List<CouponNameDTO> response = couponApiService.findCouponNames();

        return ApiResponseEntity
                .<List<CouponNameDTO>>builder()
                .data(response)
                .build();
    }

    /**
     * ADMIN: 쿠폰 지급 목록 엑셀 요청 API
     */
    @PreAuthorize("hasRole('ROLE_OWNER') " +
            "or hasRole('ROLE_PRINCIPAL_ADMIN') " +
            "or hasRole('ROLE_ADMIN')")
    @PostMapping("/issue-coupon/history-list/excel")
    public ApiResponseEntity<FileGenerationStatusIdDTO> exportIssueCouponHistoryList(@Valid @RequestBody IdsDTO dto) {
        FileGenerationStatusIdDTO result = couponApiService.exportIssueCouponHistories(dto);

        return ApiResponseEntity
                .<FileGenerationStatusIdDTO>builder()
                .data(result)
                .message("쿠폰 지급 목록 엑셀 요청 성공")
                .build();
    }

}
