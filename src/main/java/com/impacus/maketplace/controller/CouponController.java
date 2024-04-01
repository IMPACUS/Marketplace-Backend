package com.impacus.maketplace.controller;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.utils.ApiResponseEntity;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDto;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import com.impacus.maketplace.service.CouponAdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coupon")
public class CouponController {

    private final CouponAdminService couponAdminService;

    /** TODO : Admin Page 추가 쿠폰 발급
     *  1. 오픈 기념 첫 회원 가입 20% 할인 쿠폰
     *  2. 선택적 일정 (n월 회원가입 이벤트 20%할인 쿠폰 증젖ㅇ)
     *  3. (_주 회원가입 이벤트 15% 할인 쿠폱 증정)
     *  4. 친구 초대 이벤트 (ex 1주일간 친구초대 5명, 친구랑 10% 할인 크푼 증정) 보류 (가능한)
     */

    /** TODO: 기능 정의 관리자
//     * 1. 쿠폰/포인트 지급 페이지에서 회원 검색
//     *      - 아이디와 성함을 입력 시
//     *      이메일, 프로필 이미지, 등급, 휴대폰번호, 레벨 포인트, 가입일이 나와야함
     *
     * 2. 이메일, 카카오, 문자 알림 넣는 법 개발 하기                            -- 추후
     *
//     * 3. 쿠폰명을 누르면 사용가능한 쿠폰을 리스트업 하게 해야함                        -- OK
     *
//     * 4. 쿠폰명 클릭시금액과, 할인율 쿠폰 설명을 나타나게 해야함
     *
//     * 5. 수정하여 수동 지급 하는 건                                           -- 일단 보류
     *
     * 6. 쿠폰 지급 하기 기능
     *
     * 7. 회원의 가용 포인트와 레벨 포인트를 가져와야함  <- 이것 1번이랑 합치고 싶음.
     *
     *
     * 8. 포인트 지금 (레벨 포인트, 가용 포인트)
     *
     * 9. 포인트 수취 (레벨 포인트 , 가용 포인트)
     *
     *
     */

    @PostMapping("/admin/register")
    public ApiResponseEntity<Boolean> registerCouponForAdmin(@Valid @RequestBody CouponIssuedDto couponIssuedDto) {
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
     *  실제 쿠폰 및 포인트 지급 페이지 에서 사용할 정보뿌리기
     *  User 쪽 Controller 로 빼야할지 의논 필요
     */
    @PostMapping("/admin/userInfo")
    public ApiResponseEntity<CouponUserInfoResponse> getUserInfoForAdmin(@RequestBody CouponUserInfoRequest userInfoRequest) {
        //TODO: 관리자 검증 로직 추가


        ApiResponseEntity<CouponUserInfoResponse> res = new ApiResponseEntity<>();

        CouponUserInfoResponse userTargetInfo = couponAdminService.getUserTargetInfo(userInfoRequest);

        if (userTargetInfo == null) {
            res.setResult(false);
            res.setMessage(ErrorType.NOT_EXISTED_EMAIL.getMsg());
        } else {
            if (userTargetInfo.getStatus().equals(UserStatus.BLOCKED)) {
                res.setMessage(ErrorType.BLOCKED_EMAIL.getMsg());
            } else if (userTargetInfo.getStatus().equals(UserStatus.DORMANT)) {
                res.setMessage(ErrorType.NOT_ACTIVE_EMAIL.getMsg());
            }
            res.setData(userTargetInfo);
        }

        return res;
    }
    /**
     *  1.쿠폰 리스트 뿌리기
     */
    @PostMapping("/admin/couponList")
    public ApiResponseEntity<Page<CouponListDto>> getCouponList(@RequestBody CouponSearchDto couponSearchDto,
                                                                @PageableDefault(sort = {"name"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CouponListDto> result = couponAdminService.getCouponList(couponSearchDto, pageable);

        return ApiResponseEntity.<Page<CouponListDto>>builder()
                .data(result)
                .result(result.hasContent())
                .build();
    }

    /**
     *  2. 쿠폰 등록 페이지에서 쿠폰 상세 내용 입력
     */
    @PostMapping("/admin/couponDetail")
    public ApiResponseEntity<CouponDetailDto> getCouponDetail(@RequestBody CouponSearchDto couponSearchDto) {
        CouponDetailDto result = couponAdminService.getCouponDetail(couponSearchDto);

        return ApiResponseEntity.<CouponDetailDto>builder()
                .data(result)
                .build();
    }

    @PostMapping("/admin/issueCoupon")
    public ApiResponseEntity<Object> issuingCoupon(@RequestBody CouponUserIssuedDto couponUserIssuedDto) {
        return ApiResponseEntity.builder()
                .build();
    }

    @PostMapping("/admin/updateCoupon")
    public ApiResponseEntity<Object> updateCoupon(@Valid @RequestBody CouponUpdateDto couponUpdateDto) {
        boolean result = couponAdminService.updateCouponDetail(couponUpdateDto);
        return ApiResponseEntity.builder()
                .result(result)
                .build();
    }

    /** TODO: 기능 정의 사용자
     * 1.쿠폰 리스트 뿌리기
     *  - 검색 가능하게 ( 최신 순, 가격 순 )
     *  - 보유 쿠폰 개수 카운트
     *  - 쿠폰 다운로드 유무 표시 + 쿠폰 금액 + 쿠폰 이름 + XXX 구매시 사용 가능 + 쿠폰 발급 유효 기간
     *
     * 2. 쿠폰 등록하기
     *  - 쿠폰 등록 ( 쿠폰 조회시 없다면 "유효하지 않은 쿠폰입니다.\n쿠폰코드를 다시 한번 확인해주세요")
     *
     * 3. 쿠폰 다운 받기
     *
     */


}
