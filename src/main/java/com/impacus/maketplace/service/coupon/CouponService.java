package com.impacus.maketplace.service.coupon;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.impacus.maketplace.common.enumType.coupon.CouponIssuedTimeType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.coupon.request.CouponRegisterDTO;
import com.impacus.maketplace.dto.coupon.request.CouponUserSearchDTO;
import com.impacus.maketplace.dto.coupon.response.CouponUserListDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponUser;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.repository.coupon.CouponCustomRepositoryImpl;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponUserRepository couponUserRepository;
    private final ObjectCopyHelper objectCopyHelper;

    public Page<CouponUserListDTO> getCouponUserList(String searchValue, String searchOrder, Long userId, Pageable pageable) {
        return couponUserRepository.findAllCouponUserData(searchValue, searchOrder, userId, pageable);
    }

    @Transactional
    public CouponUser couponRegister(CouponRegisterDTO couponRegisterDto) {
        if (couponRegisterDto.getCouponCode().trim().length() == 10) {
            throw new CustomException(CouponErrorType.INVALID_COUPON_FORMAT);
        }
        Optional<Coupon> byCode = couponRepository.findByCode(couponRegisterDto.getCouponCode());
        if (!byCode.isPresent()) {
            throw new CustomException(CouponErrorType.NOT_EXISTED_COUPON);
        } else {
            Coupon coupon = byCode.get();
            User user = userRepository.findById(couponRegisterDto.getUserId()).orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_EMAIL));
            CouponUser couponUser = couponUserRepository.findByCouponAndUser(coupon, user);
            if (couponUser != null) {
                throw new CustomException(CouponErrorType.DUPLICATED_COUPON);
            }

            LocalDateTime couponExpireAt = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

            Long couponExpireDay = coupon.getExpireDays() + 1;

            if (coupon.getExpireDays() < 0) {
                couponExpireAt = null;
            } else {
                couponExpireAt = couponExpireAt.plusDays(couponExpireDay).minusMinutes(1);
            }

            CouponIssuedTimeType couponIssuedTime = coupon.getIssuedTimeType();
            boolean couponLock = false;

            if (couponIssuedTime == couponIssuedTime.WEEK) {
                couponLock = true;
            }


            CouponUser newCouponUser = CouponUser.builder()
                    .coupon(coupon)
                    .user(user)
                    .expiredAt(couponExpireAt)
                    .couponLock(couponLock)
                    .build();

            return couponUserRepository.save(newCouponUser);
        }
    }

    @Transactional
    public CouponUserListDTO couponDownload(Long couponUserId, Long loginUserId) {
        CouponUser validateCoupon = couponUserRepository.findByIdAndUserId(couponUserId, loginUserId);
        if (validateCoupon == null) {
            throw new CustomException(CouponErrorType.INVALID_COUPON_FORMAT);
        }
        CouponUser couponUser = couponUserRepository.findById(couponUserId)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        if (couponUser.getCouponLock() == true ||
                couponUser.getExpiredAt().isBefore(LocalDateTime.now()) ||
                couponUser.getIsDownloaded() == true ||
                couponUser.getIsUsed() == true) {
            throw new CustomException(CouponErrorType.INVALID_COUPON_REQUEST);
        }

        couponUser.setIsDownloaded(true);
        couponUserRepository.save(couponUser);

        return CouponCustomRepositoryImpl.toDto(couponUser);
    }
}
