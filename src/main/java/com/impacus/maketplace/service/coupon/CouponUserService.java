package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.coupon.response.UserCouponOverviewDTO;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.querydsl.CouponCustomRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponUserService {
    private final CouponRepository couponRepository;
    private final CouponCustomRepositroy couponCustomRepositroy;

    /**
     * 쿠폰 코드 중복 검사
     * @param code
     */
    public void duplicateCheckCode(String code) {
        if(couponRepository.existsByCode(code)) {
            throw new CustomException(new CustomException(CouponErrorType.DUPLICATED_COUPON_CODE));
        }
    }

    public List<UserCouponOverviewDTO> getUserCouponOverviewList(Long userId) {
        // 1. 사용자가 가지고 있는 쿠폰 조회
        return couponCustomRepositroy.findUserCouponOverviewList(userId);
    }
}
