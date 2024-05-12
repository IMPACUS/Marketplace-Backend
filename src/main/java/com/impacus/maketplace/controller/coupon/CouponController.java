package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponseDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserListDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponUser;
import com.impacus.maketplace.service.coupon.CouponAdminService;
import com.impacus.maketplace.service.coupon.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon")
public class CouponController {

    private final CouponAdminService couponAdminService;
    private final CouponService couponService;

    /**
     * 어드민 페이지 : 쿠폰 등록 API
     *
     * @param couponIssuedDto
     * @param user
     * @return
     */
    @PostMapping("/admin")
    public ApiResponseEntity<Boolean> registerCouponForAdmin(@Valid @RequestBody CouponIssuedDTO couponIssuedDto,
                                                             @AuthenticationPrincipal CustomUserDetails user) {
        Coupon coupon = couponAdminService.addCoupon(couponIssuedDto);

        return ApiResponseEntity.simpleResult(coupon, HttpStatus.BAD_REQUEST);
    }

    /**
     * 어드민 페이지 : 회원정보 조회 API
     *
     * @param provideTarget 지급 대상 [ 회원 검색 : TARGET_USER, 모든 회원 : TARGET_ALL ]
     * @param userEmail
     * @return
     */
    @GetMapping("/admin/user-info")
    public ApiResponseEntity<CouponUserInfoResponseDTO> getUserInfoForAdmin(@RequestParam(value = "provide-target") String provideTarget,
                                                                            @RequestParam(value = "user-email") String userEmail) {

        ApiResponseEntity<CouponUserInfoResponseDTO> res = new ApiResponseEntity<>();

        CouponUserInfoResponseDTO userTargetInfo = couponAdminService.getUserTargetInfo(provideTarget, userEmail);

        if (userTargetInfo == null) {
            res.setResult(false);
            res.setMessage(CommonErrorType.NOT_EXISTED_EMAIL.getMsg());
        } else {
            if (userTargetInfo.getStatus().equals(UserStatus.BLOCKED)) {
                res.setMessage(CommonErrorType.BLOCKED_EMAIL.getMsg());
            } else if (userTargetInfo.getStatus().equals(UserStatus.DORMANT)) {
                res.setMessage(CommonErrorType.NOT_ACTIVE_EMAIL.getMsg());
            }
            res.setData(userTargetInfo);
        }

        return res;
    }

    /**
     * 어드민 페이지 : 쿠폰 리스트 조회 API
     *
     * @param searchValue
     * @param searchOrder
     * @param pageable
     * @return
     */
    @GetMapping("/admin/list")
    public ApiResponseEntity<Page<CouponListDTO>> getCouponList(@RequestParam(value = "search-value", required = false) String searchValue,
                                                                @RequestParam(value = "search-order") String searchOrder,
                                                                @PageableDefault(size = 10, sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CouponListDTO> result = couponAdminService.getCouponList(searchValue, searchOrder, pageable);

        return ApiResponseEntity.<Page<CouponListDTO>>builder()
                .data(result)
                .result(result.hasContent())
                .build();
    }

    /**
     * 어드민 페이지 : 쿠폰 상세 조회 API
     *
     * @param couponId
     * @return
     */
    @GetMapping("/admin/detail")
    public ApiResponseEntity<CouponDetailDTO> getCouponDetail(@RequestParam(value = "coupon-id") Long couponId) {
        CouponDetailDTO result = couponAdminService.getCouponDetail(couponId);

        return ApiResponseEntity.<CouponDetailDTO>builder()
                .result(result != null ? true : false)
                .data(result)
                .build();
    }

    /**
     * 어드민 페이지 : 쿠폰 지급 API
     *
     * @param couponUserIssuedDto
     * @return
     */
    @PostMapping("/admin/provide")
    public ApiResponseEntity<Object> provideCoupon(@Valid @RequestBody CouponUserIssuedDTO couponUserIssuedDto) {
        boolean result = couponAdminService.addCouponUser(couponUserIssuedDto);
        return ApiResponseEntity
                .builder()
                .result(result)
                .build();
    }

    /**
     * 어드민 페이지 : 쿠폰 수정 API
     *
     * @param couponUpdateDto
     * @param user
     * @return
     */
    @PatchMapping("/admin")
    public ApiResponseEntity<Boolean> updateCoupon(@Valid @RequestBody CouponUpdateDTO couponUpdateDto, @AuthenticationPrincipal CustomUserDetails user) {
        Coupon coupon = couponAdminService.updateCouponDetail(couponUpdateDto);
        return ApiResponseEntity.simpleResult(coupon, HttpStatus.BAD_REQUEST);
    }

    /**
     * 유저의 보유중인 쿠폰 리스트 (검색)
     */
    /**
     * 유저 페이지 : 유저의 보유중인 쿠폰 리스트 조회 API
     * @param searchValue
     * @param searchOrder RECENT: 최근순, PRICE: 가격
     * @param pageable
     * @param user
     * @return
     */
    @GetMapping("/user/list")
    public ApiResponseEntity<Slice<CouponUserListDTO>> getCouponUserList(@RequestParam(value = "search-value") String searchValue,
                                                                         @RequestParam(value = "search-order", required = false) String searchOrder,
                                                                         @PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                         @AuthenticationPrincipal CustomUserDetails user) {
        Slice<CouponUserListDTO> result = couponService.getCouponUserList(searchValue, searchOrder, user.getId(), pageable);

        return ApiResponseEntity.<Slice<CouponUserListDTO>>builder()
                .data(result)
                .build();
    }

    /**
     * 쿠폰 등록하기
     */
    @PostMapping("/user")
    public ApiResponseEntity<Boolean> addCoupon(@Valid @RequestBody CouponRegisterDTO couponRegisterDto, @AuthenticationPrincipal CustomUserDetails user) {
        couponRegisterDto.setUserId(user.getId());
        CouponUser couponUser = couponService.couponRegister(couponRegisterDto);

        return ApiResponseEntity.simpleResult(couponUser, HttpStatus.BAD_REQUEST);
    }

    /**
     * 쿠폰 다운로드
     */

    @PostMapping("/user/download")
    public ApiResponseEntity<CouponUserListDTO> addDownload(Long couponUserId, @AuthenticationPrincipal CustomUserDetails user) {
        CouponUserListDTO data = couponService.couponDownload(couponUserId, user.getId());

        return ApiResponseEntity.<CouponUserListDTO>builder()
                .result(data != null ? true : false)
                .data(data)
                .build();
    }


}
