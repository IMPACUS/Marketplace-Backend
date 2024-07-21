package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 코드 중복 검사
     * @param code
     */
    public void duplicateCheckCode(String code) {
        if(couponRepository.existsByCode(code)) {
            throw new CustomException(new CustomException(CouponErrorType.DUPLICATED_COUPON_CODE));
        }
    }
}
