package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.coupon.request.CouponUserSearchDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserListDto;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.repository.coupon.CouponIssuanceClassificationDataRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponUserRepository couponUserRepository;
    private final CouponIssuanceClassificationDataRepository couponIssuanceClassificationDataRepository;
    private final ObjectCopyHelper objectCopyHelper;

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

    /**
     *  쿠폰 리스트 보여주는 함수
     */
    public Page<CouponUserListDto> getCouponUserList(CouponUserSearchDto couponUserSearchDto, Pageable pageable) {
        return couponUserRepository.findAllCouponUserData(couponUserSearchDto, pageable);
    }
}
