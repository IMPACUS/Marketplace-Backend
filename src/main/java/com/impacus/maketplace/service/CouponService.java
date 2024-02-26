package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.dto.coupon.request.CouponIssuedDto;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.CouponUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.impacus.maketplace.common.utils.CouponUtils.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;

    public static final String COUPON_CODE = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    /**
     * 관리자 페이지에서 등록할 쿠폰
     */
    public Boolean addCoupon(CouponIssuedDto req) {

        boolean result = false;
        {
            CouponBenefitClassification benefitClassificationType   // 혜택 구분 [ 원, % ]
                    = fromCode(CouponBenefitClassification.class, req.getCouponBenefitClassificationType());
            CouponIssuanceClassification issuanceClassificationType //  벌굽 구분 [ 그린 태그, 유저 일반, 신규고객 첫 주문 ]
                    = fromCode(CouponIssuanceClassification.class, req.getCouponIssuanceClassificationType());
            CouponPaymentTarget paymentTargetType   //  지급 대상 [ 모든 회원 , 선착순 ]
                    = fromCode(CouponPaymentTarget.class, req.getCouponPaymentTargetType());
            CouponIssuedTime issuedTimeType //  발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
                    = fromCode(CouponIssuedTime.class, req.getCouponIssuedTimeType());
            CouponExpireTime expireTimeType //  사용기간 [ 발급잉로 부터 N일, 무제한 ]
                    = fromCode(CouponExpireTime.class, req.getCouponExpireTimeType());
            CouponCoverage issuanceCoverageType //  발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
                    = fromCode(CouponCoverage.class, req.getCouponIssuanceCoverageType());
            CouponCoverage useCoverageType  //  쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
                    = fromCode(CouponCoverage.class, req.getCouponUseCoverageType());
            CouponStandardAmountType usableStandardAmountType   //  쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                    = fromCode(CouponStandardAmountType.class, req.getCouponUsableStandardAmountType());
            CouponStandardAmountType issuanceStandardAmountType //  쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                    = fromCode(CouponStandardAmountType.class, req.getCouponIssuanceStandardAmount());
            PaymentMethod paymentMethodType //  결제수단 [ 카드, XXX 페이, 간편결제 ]
                    = PaymentMethod.fromCode(Integer.valueOf(req.getPaymentMethodType()));
            CouponIssuancePeriodType issuancePeriodType //  기간 설정 [ 기간내 N 회 이상 주문시 , 지정 기간 없음 (지속적인 기준) ]
                    = fromCode(CouponIssuancePeriodType.class, req.getCouponIssuancePeriodType());
            CouponIssuanceType issuanceType //  자동 / 수동 발급 [ 자동, 수동 ]
                    = fromCode(CouponIssuanceType.class, req.getCouponIssuanceType());
        }

        Coupon newCoupon = Coupon.builder()
                .code(getUUIDCouponCode())
                .benefitAmount(req.getBenefitAmount())
                .name(req.getName())
                .description(req.getDesc())
                .build();

//        Coupon coupon = Coupon.builder()
//                .code(CouponUtils.getUUIDCouponCode())
//                .expiredAt(stringToLocalDateTime(couponIssuedDto.getExpiredAt()))
//                .discount(couponIssuedDto.getDiscount())
//                .couponType(couponIssuedDto.getCouponTypeEnum())
//                .constraints(couponIssuedDto.getConstraints())
//                .build();
//        coupon = couponRepository.save(coupon);

//        return coupon != null ? true : false;
        return null;
    }


    // 선착순 분산 락
//    public CreateMemberCouponResponse createMemberCoupon(CreateMemberCouponCommand command) {
//        RLock lock = redissonClient.getLock(couponLockName);
//
//        try {
//            if (!lock.tryLock(10, 3, TimeUnit.SECONDS)) {
//                throw new RuntimeException("Lock 획득 실패");
//            }
//
//            // Lock 획득했으므로 아래부터는 비즈니스 로직 처리
//            if (isNotStock(command.getCouponId())) {
//                throw new CouponNotRemainException();
//            }
//
//            if (isDuplicateCoupon(command)) {
//                throw new DuplicateCouponException();
//            }
//
//            updateCouponStatePort.decreaseRemainQuantity(command.getCouponId());
//            MemberCoupon memberCoupon = createMemberCouponPort.createMemberCoupon(command.getMemberId(), command.getCouponId());
//            return new CreateMemberCouponResponse(memberCoupon.getId(), command.getCouponId(), memberCoupon.getCreateDateTime());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            if (lock != null && lock.isLocked()) {
//                lock.unlock();
//            }
//        }
//    }




}
