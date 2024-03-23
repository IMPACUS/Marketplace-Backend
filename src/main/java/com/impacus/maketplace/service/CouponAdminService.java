package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.coupon.request.CouponIssuedDto;
import com.impacus.maketplace.dto.coupon.request.CouponSearchDto;
import com.impacus.maketplace.dto.coupon.request.CouponUserInfoRequest;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDto;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponIssuanceClassificationData;
import com.impacus.maketplace.repository.CouponUserRepository;
import com.impacus.maketplace.repository.coupon.CouponIssuanceClassificationDataRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.impacus.maketplace.common.utils.CouponUtils.fromCode;
import static com.impacus.maketplace.common.utils.CouponUtils.getUUIDCouponCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponAdminService {

    private final CouponRepository couponRepository;
    private final CouponUserRepository couponUserRepository;
    private final CouponIssuanceClassificationDataRepository couponIssuanceClassificationDataRepository;
    private final ObjectCopyHelper objectCopyHelper;

    public static final String COUPON_CODE = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    /**
     * 관리자 페이지에서 등록할 쿠폰
     */
    @Transactional
    public Boolean addCoupon(CouponIssuedDto req) {

        boolean result = true;

        try {
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


            Coupon newCoupon = Coupon.builder()
                    .name(req.getName())
                    .description(req.getDesc())
                    .couponBenefitClassification(benefitClassificationType)
                    .benefitAmount(req.getBenefitAmount())
                    .couponIssuanceClassification(issuanceClassificationType)
                    .couponPaymentTarget(paymentTargetType)
                    // 지급 대상 중 선착순 일경우 선착순 명수
                    .firstComeFirstServedAmount(req.getFirstComeFirstServedAmount())
                    .couponIssuedTime(issuedTimeType)
                    .couponExpireTime(expireTimeType)
                    .couponIssuanceCoverage(issuanceCoverageType)
                    .couponUseCoverage(useCoverageType)
                    .couponUsableStandardAmount(usableStandardAmountType)
                    .usableStandardMount(req.getUsableStandardMount())
                    .paymentMethod(paymentMethodType)
                    .numberOfWithPeriod(req.getNumberOfWithPeriod())
                    .couponIssuanceStandardAmount(issuanceStandardAmountType)
                    .issueStandardMount(req.getIssueStandardMount())
                    .couponIssuancePeriod(issuancePeriodType)
                    .couponIssuance(issuanceType)
                    .code(getUUIDCouponCode())
                    .loginCouponIssueNotification(req.getLoginCouponIssueNotification())
                    .issuingCouponsSendSMS(req.getIssuingCouponsSendSMS())
                    .issuanceCouponSendEmail(req.getIssuingCouponsSendSMS())
                    .build();

            // 쿠폰 발급 구분 중 구분 상세
            if (req.getCouponIssuanceClassificationData() != null) {
                CouponIssuanceClassificationData couponIssuanceClassificationData =
                        couponIssuanceClassificationDataRepository.findById(req.getCouponIssuanceClassificationData())
                                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_BRAND));
                newCoupon.setCouponIssuanceClassificationData(couponIssuanceClassificationData);
            }

            // 기간 설정 상세
            switch (issuancePeriodType) {
                case CIP_1 -> { // 지정 기간 설정
                    newCoupon.setStartIssuanceAt(LocalDate.parse(req.getStartIssuanceAt(), DateTimeFormatter.ISO_DATE));
                    newCoupon.setEndIssuanceAt(LocalDate.parse(req.getEndIssuanceAt(), DateTimeFormatter.ISO_DATE));
                }
                case CIP_2 -> { // 지정 기간 없음 (지속적인 기준)

                }
            }
            couponRepository.save(newCoupon);
        } catch (Exception e) {
            return false;
        }
        return result;
    }

    @Transactional
    public CouponUserInfoResponse getUserTargetInfo(CouponUserInfoRequest req) {
        if (!ProvisionTarget.USER.getCode().equals(req.getProvisionTarget())) {
            return null;
        } else {
            return couponRepository.findByAddCouponInfo(req);
        }
    }

    public Page<CouponListDto> getCouponList(CouponSearchDto couponSearchDto, Pageable pageable) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            Page<CouponListDto> dataList = couponRepository.findAllCouponList(couponSearchDto, pageable);
            if (!couponSearchDto.getSearchNotStop()) {
                dataList.forEach(data -> {
                    if (data.getCouponIssuanceStandardAmount() == CouponStandardAmountType.CSA_1) {
                        data.setIssuanceStandard(CouponStandardAmountType.CSA_1.getValue());
                    } else if (data.getCouponIssuanceStandardAmount() == CouponStandardAmountType.CSA_2) {
                        String number = String.valueOf(data.getIssueStandardMount().intValue());
                        number = StringUtils.updateNumberFormat(number);
                        String issuanceStandard = CouponStandardAmountType.CSA_2.getValue().replace("N", String.valueOf(number));
                        data.setIssuanceStandard(issuanceStandard);
                    }

                    if (data.getCouponExpireTime() == CouponExpireTime.CET_2) {
                        data.setExpiredPeriod(CouponExpireTime.CET_2.getValue());
                    } else if (data.getCouponExpireTime() == CouponExpireTime.CET_1) {
                        Long number = data.getExpireDays();
                        String expiredPeriod = CouponExpireTime.CET_1.getValue().replace("N", String.valueOf(number));
                        data.setExpiredPeriod(expiredPeriod);
                    }

                    if (data.getCouponPaymentTarget() == CouponPaymentTarget.CPT_1) {
                        data.setNumberOfIssuance(CouponPaymentTarget.CPT_1.getValue());
                    } else if (data.getCouponPaymentTarget() == CouponPaymentTarget.CPT_2) {
                        data.setNumberOfIssuance(String.valueOf(data.getFirstComeFirstServedAmount()));
                    }

                    data.setManualOrAutomatic(data.getCouponIssuance().getValue());
                    data.setIssuanceStatus(data.getStatus().getValue());
                    data.setRecentActivity(dtf.format(data.getModifyAt()));
                });
            }
            return dataList;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    public CouponDetailDto getCouponDetail(CouponSearchDto couponSearchDto) {
        try {
            Coupon data = couponRepository.findById(couponSearchDto.getId())
                    .orElseThrow(() -> new CustomException(ErrorType.INVALID_COUPON_FORMAT));
            CouponDetailDto result = objectCopyHelper.copyObject(data, CouponDetailDto.class);

            return result;
        } catch (Exception e) {
            throw new CustomException(e);
        }
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
