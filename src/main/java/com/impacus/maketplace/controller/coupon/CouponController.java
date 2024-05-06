package com.impacus.maketplace.controller.coupon;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponseDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserListDTO;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.CustomUserDetails;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon")
public class CouponController {

    private final CouponAdminService couponAdminService;
    private final CouponService couponService;

    @PostMapping("/admin/register")
    public ApiResponseEntity<Boolean> registerCouponForAdmin(@Valid @RequestBody CouponIssuedDTO couponIssuedDto, @AuthenticationPrincipal CustomUserDetails user) {
        //TODO: 관리자 검증 로직 추가
        Boolean result = couponAdminService.addCoupon(couponIssuedDto);

        String message = "";
        if (!result) {
            message = "쿠폰 발급 오류";
        }
        return ApiResponseEntity.<Boolean>builder()
                .result(result)
                .message(message)
                .data(result)
                .build();
    }

    /**
     * 실제 쿠폰 및 포인트 지급 페이지 에서 사용할 정보뿌리기
     * User 쪽 Controller 로 빼야할지 의논 필요
     */
    @PostMapping("/admin/userInfo")
    public ApiResponseEntity<CouponUserInfoResponseDTO> getUserInfoForAdmin(@RequestBody CouponUserInfoRequestDTO userInfoRequest, @AuthenticationPrincipal CustomUserDetails user) {
        //TODO: 관리자 검증 로직 추가


        ApiResponseEntity<CouponUserInfoResponseDTO> res = new ApiResponseEntity<>();

        CouponUserInfoResponseDTO userTargetInfo = couponAdminService.getUserTargetInfo(userInfoRequest);

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
     * 1.쿠폰 리스트 뿌리기
     */
    @PostMapping("/admin/couponList")
    public ApiResponseEntity<Page<CouponListDTO>> getCouponList(@RequestBody CouponSearchDTO couponSearchDto,
                                                                @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable,
                                                                @AuthenticationPrincipal CustomUserDetails user) {
        Page<CouponListDTO> result = couponAdminService.getCouponList(couponSearchDto, pageable);

        return ApiResponseEntity.<Page<CouponListDTO>>builder()
                .data(result)
                .result(result.hasContent())
                .build();
    }

    /**
     * 2. 쿠폰 등록 페이지에서 쿠폰 상세 내용 입력
     */
    @PostMapping("/admin/couponDetail")
    public ApiResponseEntity<CouponDetailDTO> getCouponDetail(@RequestBody CouponSearchDTO couponSearchDto, @AuthenticationPrincipal CustomUserDetails user) {
        CouponDetailDTO result = couponAdminService.getCouponDetail(couponSearchDto);

        return ApiResponseEntity.<CouponDetailDTO>builder()
                .result(result != null ? true : false)
                .data(result)
                .build();
    }

    @PostMapping("/admin/issueCoupon")
    public ApiResponseEntity<Object> issuingCoupon(@RequestBody CouponUserIssuedDTO couponUserIssuedDto, @AuthenticationPrincipal CustomUserDetails user) {
        return ApiResponseEntity.builder()
                .build();
    }

    @PostMapping("/admin/updateCoupon")
    public ApiResponseEntity<Object> updateCoupon(@Valid @RequestBody CouponUpdateDTO couponUpdateDto, @AuthenticationPrincipal CustomUserDetails user) {
        boolean result = couponAdminService.updateCouponDetail(couponUpdateDto);
        return ApiResponseEntity.builder()
                .result(result)
                .build();
    }

    /**
     * ADMIN 쿠폰 지급 API
     */
    @PostMapping("/admin/provide")
    public ApiResponseEntity<Object> provideCoupon(@Valid @RequestBody CouponUserIssuedDTO couponUserIssuedDto, @AuthenticationPrincipal CustomUserDetails user) {
        boolean result = couponAdminService.addCouponUser(couponUserIssuedDto);
        return ApiResponseEntity
                .builder()
                .result(result)
                .build();
    }

    /**
     * 유저의 보유중인 쿠폰 리스트 (검색)
     */
    @PostMapping("/user/list")
    public ApiResponseEntity<Slice<CouponUserListDTO>> getCouponUserList(@Valid @RequestBody CouponUserSearchDTO couponUserSearchDto,
                                                                         @PageableDefault(size = 10, sort = "createAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                         @AuthenticationPrincipal CustomUserDetails user) {
        couponUserSearchDto.setUserId(user.getId());
        Slice<CouponUserListDTO> result = couponService.getCouponUserList(couponUserSearchDto, pageable);

        return ApiResponseEntity.<Slice<CouponUserListDTO>>builder()
                .data(result)
                .build();
    }

    /**
     * 쿠폰 등록하기
     */
    @PostMapping("/user/register")
    public ApiResponseEntity<Object> registerCouponForUser(@Valid @RequestBody CouponRegisterDTO couponRegisterDto, @AuthenticationPrincipal CustomUserDetails user) {
        couponRegisterDto.setUserId(user.getId());
        boolean result = couponService.couponRegister(couponRegisterDto);

        return ApiResponseEntity.<Object>builder()
                .result(result)
                .data(result)
                .build();
    }

    /**
     * 쿠폰 다운로드
     */

    @PostMapping("/user/download")
    public ApiResponseEntity<CouponUserListDTO> couponDownload(Long couponUserId, @AuthenticationPrincipal CustomUserDetails user) {
        CouponUserListDTO data = couponService.couponDownload(couponUserId, user.getId());

        return ApiResponseEntity.<CouponUserListDTO>builder()
                .result(data != null ? true : false)
                .data(data)
                .build();
    }


}
