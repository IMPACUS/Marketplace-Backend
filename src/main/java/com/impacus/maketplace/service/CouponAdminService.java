package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDto;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponIssuanceClassificationData;
import com.impacus.maketplace.entity.coupon.CouponUser;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.CouponUserRepository;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.repository.coupon.CouponIssuanceClassificationDataRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.impacus.maketplace.common.utils.CouponUtils.fromCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponAdminService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
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
        CouponIssuancePeriodType issuancePeriodType //  기간 설정 [ 기간내 N 회 이상 주문시 , 지정 기간 없음 (지속적인 기준) ]
                = fromCode(CouponIssuancePeriodType.class, req.getCouponIssuancePeriodType());
        CouponIssuanceType issuanceType //  자동 / 수동 발급 [ 자동, 수동 ]
                = fromCode(CouponIssuanceType.class, req.getCouponIssuanceType());
        CouponType couponType           // 쿠폰 타입 [ 이벤트, 지급형 ]
                = fromCode(CouponType.class, req.getCouponType());


        Coupon coupon = new Coupon();
        coupon.setName(req.getName());
        coupon.setDescription(req.getDesc());
        coupon.setCouponBenefitClassification(benefitClassificationType);
        coupon.setBenefitAmount(req.getBenefitAmount());

        coupon.setCouponIssuanceClassification(issuanceClassificationType);
        switch (issuanceClassificationType) {
            case CIC_1, CIC_2 -> {
                CouponIssuanceClassificationData couponIssuanceClassificationData = couponIssuanceClassificationDataRepository.findById(req.getCouponIssuanceClassificationData())
                        .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_ISSUANCE));
                coupon.setCouponIssuanceClassificationData(couponIssuanceClassificationData);
            }
            case CIC_3, CIC_4 -> {
                // TODO: 추후 개발 예정
            }
        }
        coupon.setCouponPaymentTarget(paymentTargetType);
        if (paymentTargetType == CouponPaymentTarget.CPT_2 && req.getFirstComeFirstServedAmount() > 0) {
            coupon.setFirstComeFirstServedAmount(req.getFirstComeFirstServedAmount());
        } else {
            coupon.setFirstComeFirstServedAmount(-1L);
        }

        coupon.setCouponIssuedTime(issuedTimeType);
        coupon.setCouponType(couponType);

        coupon.setCouponExpireTime(expireTimeType);
        if (expireTimeType == CouponExpireTime.CET_1 && req.getExpireDays() > 0) {
            coupon.setExpireDays(req.getExpireDays());
        } else {
            coupon.setExpireDays(-1L);
        }

        //TODO: Temp Fix
        coupon.setCouponIssuanceCoverage(issuanceCoverageType);
        coupon.setCouponUseCoverage(useCoverageType);
        //

        coupon.setCouponUsableStandardAmountType(usableStandardAmountType);
        if (usableStandardAmountType == CouponStandardAmountType.CSA_2 && req.getUsableStandardMount() > 0) {
            coupon.setUsableStandardAmount(req.getUsableStandardMount());
        } else {
            coupon.setUsableStandardAmount(-1);
        }

        coupon.setCouponIssuanceStandardAmount(issuanceStandardAmountType);
        if (issuanceStandardAmountType == CouponStandardAmountType.CSA_2 && req.getIssueStandardMount() > 0) {
            coupon.setIssueStandardMount(req.getIssueStandardMount());
        } else {
            coupon.setIssueStandardMount(-1);
        }

        coupon.setCouponIssuancePeriod(issuancePeriodType);
        if (issuancePeriodType == CouponIssuancePeriodType.CIP_1) {
            if (req.getStartIssuanceAt() != null && req.getEndIssuanceAt() != null && req.getNumberOfWithPeriod() != null) {
                LocalDate startAt = LocalDate.parse(req.getStartIssuanceAt(), DateTimeFormatter.ISO_DATE);
                coupon.setStartIssuanceAt(startAt);
                LocalDate endAt = LocalDate.parse(req.getEndIssuanceAt(), DateTimeFormatter.ISO_DATE);
                coupon.setEndIssuanceAt(endAt);

                coupon.setNumberOfWithPeriod(req.getNumberOfWithPeriod());
            }
        } else {
            coupon.setStartIssuanceAt(null);
            coupon.setEndIssuanceAt(null);
            coupon.setNumberOfWithPeriod(req.getNumberOfWithPeriod());
        }

        coupon.setCouponIssuance(issuanceType);

        coupon.setLoginCouponIssueNotification(req.getLoginCouponIssueNotification());
        coupon.setIssuingCouponsSendSMS(req.getIssuingCouponsSendSMS());
        coupon.setIssuanceCouponSendEmail(req.getIssuanceCouponSendEmail());

        String couponCode = CouponUtils.generateCode();
        coupon.setCode(couponCode);

        couponRepository.save(coupon);

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
                    if (data.getCouponIssuanceStandardAmountType() == CouponStandardAmountType.CSA_1) {
                        data.setIssuanceStandard(CouponStandardAmountType.CSA_1.getValue());
                    } else if (data.getCouponIssuanceStandardAmountType() == CouponStandardAmountType.CSA_2) {
                        String number = String.valueOf(data.getIssueStandardAmount());
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

            return CouponDetailDto.entityToDto(data);
        } catch (CustomException e) {
            return null;
        }
    }

    @Transactional
    public boolean updateCouponDetail(CouponUpdateDto req) {
        CouponBenefitClassification benefitClassificationType   // 혜택 구분 [ 원, % ]
                = fromCode(CouponBenefitClassification.class, req.getCouponBenefitClassificationType());
        CouponIssuanceClassification issuanceClassificationType //  벌굽 구분 [ 그린 태그, 유저 일반, 신규고객 첫 주문 ]
                = fromCode(CouponIssuanceClassification.class, req.getCouponIssuanceClassificationType());
        CouponPaymentTarget paymentTargetType   //  지급 대상 [ 모든 회원 , 선착순 ]
                = fromCode(CouponPaymentTarget.class, req.getCouponPaymentTargetType());
        CouponIssuedTime issuedTimeType //  발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
                = fromCode(CouponIssuedTime.class, req.getCouponIssuedTimeType());
        CouponCoverage issuanceCoverageType //  발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
                = fromCode(CouponCoverage.class, req.getCouponIssuanceCoverageType());
        CouponCoverage useCoverageType  //  쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
                = fromCode(CouponCoverage.class, req.getCouponUseCoverageType());
        CouponStandardAmountType usableStandardAmountType   //  쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                = fromCode(CouponStandardAmountType.class, req.getCouponUsableStandardAmountType());
        CouponStandardAmountType issuanceStandardAmountType //  쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                = fromCode(CouponStandardAmountType.class, req.getCouponIssuanceStandardAmountType());
        CouponIssuancePeriodType issuancePeriodType //  기간 설정 [ 기간내 N 회 이상 주문시 , 지정 기간 없음 (지속적인 기준) ]
                = fromCode(CouponIssuancePeriodType.class, req.getCouponIssuancePeriodType());
        CouponIssuanceType issuanceType //  자동 / 수동 발급 [ 자동, 수동 ]
                = fromCode(CouponIssuanceType.class, req.getCouponIssuanceType());
        CouponType couponType           // 쿠폰 타입 [ 이벤트, 지급형 ]
                = fromCode(CouponType.class, req.getCouponType());

        Coupon coupon = couponRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(ErrorType.INVALID_COUPON_FORMAT));

        coupon.setCouponBenefitClassification(benefitClassificationType);
        coupon.setBenefitAmount(req.getBenefitAmount());

        coupon.setCouponIssuanceClassification(issuanceClassificationType);
        switch (issuanceClassificationType) {
            case CIC_1, CIC_2 -> {
                CouponIssuanceClassificationData couponIssuanceClassificationData = couponIssuanceClassificationDataRepository.findById(req.getCouponIssuanceClassificationData())
                        .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_ISSUANCE));
                coupon.setCouponIssuanceClassificationData(couponIssuanceClassificationData);
            }
            case CIC_3, CIC_4 -> {
                // TODO: 추후 개발 예정
            }
        }
        coupon.setCouponPaymentTarget(paymentTargetType);
        if (paymentTargetType == CouponPaymentTarget.CPT_2 && req.getFirstComeFirstServedAmount() > 0) {
            coupon.setFirstComeFirstServedAmount(req.getFirstComeFirstServedAmount());
        } else {
            coupon.setFirstComeFirstServedAmount(-1L);
        }
        coupon.setCouponIssuedTime(issuedTimeType);
        coupon.setCouponType(couponType);

        //TODO: Temp Fix
        coupon.setCouponIssuanceCoverage(issuanceCoverageType);
        coupon.setCouponUseCoverage(useCoverageType);
        //

        coupon.setCouponUsableStandardAmountType(usableStandardAmountType);
        if (usableStandardAmountType == CouponStandardAmountType.CSA_2 && req.getUsableStandardAmount() > 0) {
            coupon.setUsableStandardAmount(req.getUsableStandardAmount());
        } else {
            coupon.setUsableStandardAmount(-1);
        }

        coupon.setCouponIssuanceStandardAmount(issuanceStandardAmountType);
        if (issuanceStandardAmountType == CouponStandardAmountType.CSA_2 && req.getIssueStandardAmount() > 0) {
            coupon.setIssueStandardMount(req.getIssueStandardAmount());
        } else {
            coupon.setIssueStandardMount(-1);
        }

        coupon.setCouponIssuancePeriod(issuancePeriodType);
        if (issuancePeriodType == CouponIssuancePeriodType.CIP_1) {
            if (req.getStartIssuanceAt() != null && req.getEndIssuanceAt() != null && req.getNumberOfWithPeriod() != null) {
                LocalDate startAt = LocalDate.parse(req.getStartIssuanceAt(), DateTimeFormatter.ISO_DATE);
                coupon.setStartIssuanceAt(startAt);
                LocalDate endAt = LocalDate.parse(req.getEndIssuanceAt(), DateTimeFormatter.ISO_DATE);
                coupon.setEndIssuanceAt(endAt);

                coupon.setNumberOfWithPeriod(req.getNumberOfWithPeriod());
            }
        } else {
            coupon.setStartIssuanceAt(null);
            coupon.setEndIssuanceAt(null);
            coupon.setNumberOfWithPeriod(req.getNumberOfWithPeriod());
        }

        coupon.setCouponIssuance(issuanceType);

        coupon.setLoginCouponIssueNotification(req.getLoginCouponIssueNotification());
        coupon.setIssuingCouponsSendSMS(req.getIssuingCouponsSendSMS());
        coupon.setIssuanceCouponSendEmail(req.getIssuanceCouponSendEmail());

        return true;
    }

    @Transactional
    public boolean addCouponUser(CouponUserIssuedDto couponUserIssuedDto) {
        Coupon coupon;
        LocalDateTime couponExpireAt = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        try {
            if (couponUserIssuedDto.getCouponId() != null) {
                coupon = couponRepository.findById(couponUserIssuedDto.getCouponId())
                        .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_COUPON, ErrorType.NOT_EXISTED_COUPON.getMsg()));
            } else {
                return false;
            }

            if (couponUserIssuedDto.getCouponTarget().equals(CouponTargetType.USER.getCode())) {
                User user = userRepository.findById(couponUserIssuedDto.getUserId())
                        .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_EMAIL));


                Long couponExpireDay = coupon.getExpireDays() + 1; // 23:59분 을 위해
                if (couponExpireDay < 0) {
                    couponExpireAt = null;
                } else {
                    couponExpireAt = couponExpireAt.plusDays(couponExpireDay).minusMinutes(1);
                }

                CouponIssuedTime couponIssuedTime = coupon.getCouponIssuedTime();
                boolean couponLock = false;

                if (couponIssuedTime == CouponIssuedTime.CIT_1) {   // 1주일 뒤 발급 따라서 기본적으로 lock, 조건충족시 해제
                    couponLock = true;
                }

                CouponUser couponUser = CouponUser.builder()
                        .coupon(coupon)
                        .user(user)
                        .expiredAt(couponExpireAt)
                        .couponLock(couponLock)
                        .build();

                couponUserRepository.save(couponUser);
            } else if (couponUserIssuedDto.getCouponTarget().equals(CouponTargetType.ALL.getCode())) {
                List<User> userList = userRepository.findAll();

                Long couponExpireDay = coupon.getExpireDays() + 1; // 23:59분 을 위해
                if (couponExpireDay < 0) {
                    couponExpireAt = null;
                } else {
                    couponExpireAt = couponExpireAt.plusDays(couponExpireDay).minusMinutes(1);
                }
                final LocalDateTime resultCouponExpireAt = couponExpireAt;
                CouponIssuedTime couponIssuedTime = coupon.getCouponIssuedTime();


                boolean couponLock = false;

                if (couponIssuedTime == CouponIssuedTime.CIT_1) {   // 1주일 뒤 발급 따라서 기본적으로 lock, 조건충족시 해제
                    couponLock = true;
                }
                final boolean resultCouponLock = couponLock;

                userList.parallelStream().forEach((user) -> {
                    CouponUser couponUser = CouponUser.builder()
                            .coupon(coupon)
                            .user(user)
                            .expiredAt(resultCouponExpireAt)
                            .couponLock(resultCouponLock)
                            .build();

                    couponUserRepository.save(couponUser);
                });
            } else {
                return false;
            }
            //TODO: 알림 서비스
            couponAlarm(couponUserIssuedDto.getAlarmType());
            return true;
        } catch (CustomException e) {
            return false;
        }

    }

    public void couponAlarm(String alarmTypeCode) {
        CouponProvideAlarmType couponProvideAlarmType = CouponProvideAlarmType.fromCode(alarmTypeCode);
        switch (couponProvideAlarmType) {
            case EMAIL_ALARM -> {
                //TODO: 이메일 알림 개발 예정
            }
            case KAKAO_ALARM -> {
                //TODO: 카카오 알림 개발 예정
            }
            case SMS_ALARM -> {
                //TODO: SMS 알림 개발 예정
            }
            case UNKOWN -> {
                throw new CustomException(ErrorType.INVALID_ALARM);
            }
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
