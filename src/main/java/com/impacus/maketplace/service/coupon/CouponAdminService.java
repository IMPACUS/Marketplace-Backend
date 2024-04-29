package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.coupon.request.*;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDto;
import com.impacus.maketplace.dto.coupon.response.CouponListDto;
import com.impacus.maketplace.dto.coupon.response.CouponUserInfoResponse;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.coupon.CouponUser;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.CouponUserRepository;
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
import java.util.Optional;

import static com.impacus.maketplace.common.utils.CouponUtils.fromCode;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponAdminService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponUserRepository couponUserRepository;
    private final CouponService couponService;


//    public static final String COUPON_CODE = "^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";


    /**
     * 관리자 페이지에서 쿠폰을 등록하는 함수
     */
    @Transactional
    public Boolean addCoupon(CouponIssuedDto req) {

        boolean result = true;

        CouponBenefitType benefitType   // 혜택 구분 [ 원, % ]
                = fromCode(CouponBenefitType.class, req.getBenefitType());
        CouponProductTargetType productTargetType   // 에코 할인 여부 [일반, 에코/그린, 상관없음]
                = fromCode(CouponProductTargetType.class, req.getProductTargetType());
        CouponPaymentTargetType paymentTargetType   //  지급 대상 [ 모든 회원 , 선착순 ]
                = fromCode(CouponPaymentTargetType.class, req.getCouponPaymentTargetType());
        CouponIssuedTimeType issuedTimeType //  발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
                = fromCode(CouponIssuedTimeType.class, req.getIssuedTimeType());
        CouponExpireTimeType expireTimeType //  사용기간 [ 발급잉로 부터 N일, 무제한 ]
                = fromCode(CouponExpireTimeType.class, req.getExpireTimeType());
        CouponCoverageType issueCoverageType //  발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
                = fromCode(CouponCoverageType.class, req.getIssueCoverageType());
        CouponCoverageType useCoverageType  //  쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
                = fromCode(CouponCoverageType.class, req.getUseCoverageType());
        CouponStandardType useStandardType   //  쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                = fromCode(CouponStandardType.class, req.getUseStandardType());
        CouponStandardType issueStandardType //  쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                = fromCode(CouponStandardType.class, req.getIssueStandardType());
        CouponPeriodType periodType //  기간 설정 [ 기간내 N 회 이상 주문시 , 지정 기간 없음 (지속적인 기준) ]
                = fromCode(CouponPeriodType.class, req.getPeriodType());
        CouponAutoManualType autoManualType //  자동 / 수동 발급 [ 자동, 수동 ]
                = fromCode(CouponAutoManualType.class, req.getAutoManualType());
        CouponType couponType           // 쿠폰 타입 [ 이벤트, 지급형 ]
                = fromCode(CouponType.class, req.getType());


        Coupon coupon = new Coupon();
        // 쿠폰코드 수동 입력될 경우
        if (req.getCode() != null) {
            Optional<Coupon> findCode = couponRepository.findByCode(req.getCode());
            if (findCode.isPresent()) {
                throw new CustomException(CommonErrorType.DUPLICATED_COUPON_CODE);
            } else {
                coupon.setCode(req.getCode());
            }
        } else { // 자동 입력
            String couponCode = generateCode();
            coupon.setCode(couponCode);
        }


        coupon.setName(req.getName());
        coupon.setDescription(req.getDescription());
        coupon.setBenefitType(benefitType);
        // 퍼센트 할인일 때 100%가 넘어가는 경우 throw
        if (benefitType == CouponBenefitType.PERCENTAGE && req.getBenefitValue() > 100) {
            throw new CustomException(CommonErrorType.INVALID_PERCENT);
        }
        if (req.getBenefitValue() > 0) {
            coupon.setBenefitValue(req.getBenefitValue());
        } else {
            throw new CustomException(CommonErrorType.INVALID_VALUE);
        }

        coupon.setProductTargetType(productTargetType);

        coupon.setPaymentTargetType(paymentTargetType);
        if (paymentTargetType == CouponPaymentTargetType.FIRST) {
            if (req.getFirstCount() > 0) {
                coupon.setFirstCount(req.getFirstCount());
            } else {
                throw new CustomException(CommonErrorType.INVALID_FIRST_COUNT);
            }
        } else if (paymentTargetType == CouponPaymentTargetType.ALL) {
            coupon.setFirstCount(null);
        }

        coupon.setIssuedTimeType(issuedTimeType);
        coupon.setType(couponType);

        coupon.setExpireTimeType(expireTimeType);
        if (expireTimeType == CouponExpireTimeType.LIMIT && req.getExpireDays() > 0) {
            coupon.setExpireDays(req.getExpireDays());
        } else {
            coupon.setExpireDays(null);
        }

        //TODO: Temp Fix
        coupon.setIssueCoverageType(issueCoverageType);
        coupon.setUseCoverageType(useCoverageType);

        coupon.setUseStandardType(useStandardType);
        if (useStandardType == CouponStandardType.LIMIT) {
            if (req.getUseStandardValue() >= 0) {
                coupon.setUseStandardValue(req.getUseStandardValue());
            } else {
                throw new CustomException(CommonErrorType.INVALID_VALUE);
            }
        } else {
            coupon.setUseStandardValue(null);
        }

        coupon.setIssueStandardType(issueStandardType);
        if (issueStandardType == CouponStandardType.LIMIT) {
            if (req.getIssueStandardValue() >= 0) {
                coupon.setIssueStandardValue(req.getIssueStandardValue());
            } else {
                throw new CustomException(CommonErrorType.INVALID_VALUE);
            }
        } else {
            coupon.setIssueStandardValue(null);
        }

        coupon.setPeriodType(periodType);
        if (periodType == CouponPeriodType.SET) {
            if (req.getPeriodStartAt() != null && req.getPeriodEndAt() != null && req.getNumberOfPeriod() != null) {
                LocalDate startAt = LocalDate.parse(req.getPeriodStartAt(), DateTimeFormatter.ISO_DATE);
                LocalDate endAt = LocalDate.parse(req.getPeriodEndAt(), DateTimeFormatter.ISO_DATE);

                coupon.setPeriodStartAt(startAt);
                coupon.setPeriodEndAt(endAt);
                coupon.setNumberOfPeriod(req.getNumberOfPeriod());
            } else {
                throw new CustomException(CommonErrorType.INVALID_VALUE);
            }
        } else {
            coupon.setPeriodStartAt(null);
            coupon.setPeriodEndAt(null);
            coupon.setNumberOfPeriod(req.getNumberOfPeriod());
        }

        coupon.setAutoManualType(autoManualType);

        coupon.setLoginAlert(req.getLoginAlert());
        coupon.setSmsAlert(req.getSmsAlert());
        coupon.setEmailAlert(req.getEmailAlert());

        couponRepository.save(coupon);

        return result;
    }

    /**
     * ADMIN 쿠폰 페이지에서 회원 정보 조회 해오는 함수
     */
    @Transactional
    public CouponUserInfoResponse getUserTargetInfo(CouponUserInfoRequest req) {
        if (!ProvisionTarget.USER.getCode().equals(req.getProvisionTarget())) {
            return null;
        } else {
            return couponRepository.findByAddCouponInfo(req);
        }
    }

    /**
     * ADMIN 페이지에서 쿠폰 리스트 불로오는 함수
     */

    public Page<CouponListDto> getCouponList(CouponSearchDto couponSearchDto, Pageable pageable) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            Page<CouponListDto> dataList = couponRepository.findAllCouponList(couponSearchDto, pageable);
            if (!couponSearchDto.getSearchNotStop()) {
                dataList.forEach(data -> {
                    if (data.getIssueStandardType() == CouponStandardType.UNLIMITED) {
                        data.setIssuanceStandard(CouponStandardType.UNLIMITED.getValue());
                    } else if (data.getIssueStandardType() == CouponStandardType.LIMIT) {
                        String number = String.valueOf(data.getIssueStandardValue());
                        number = StringUtils.updateNumberFormat(number);
                        String issuanceStandard = CouponStandardType.LIMIT.getValue().replace("N", String.valueOf(number));
                        data.setIssuanceStandard(issuanceStandard);
                    }

                    if (data.getExpireTimeType() == CouponExpireTimeType.UNLIMITED) {
                        data.setExpiredPeriod(CouponExpireTimeType.UNLIMITED.getValue());
                    } else if (data.getExpireTimeType() == CouponExpireTimeType.LIMIT) {
                        Long number = data.getExpireDays();
                        String expiredPeriod = CouponExpireTimeType.LIMIT.getValue().replace("N", String.valueOf(number));
                        data.setExpiredPeriod(expiredPeriod);
                    }

                    if (data.getPaymentTargetType() == CouponPaymentTargetType.ALL) {
                        data.setNumberOfIssuance(CouponPaymentTargetType.ALL.getValue());
                    } else if (data.getPaymentTargetType() == CouponPaymentTargetType.FIRST) {
                        data.setNumberOfIssuance(String.valueOf(data.getFirstCount()));
                    }

                    data.setManualOrAutomatic(data.getAutoManualType().getValue());
                    data.setIssuanceStatus(data.getStatusType().getValue());
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
                    .orElseThrow(() -> new CustomException(CommonErrorType.INVALID_COUPON_FORMAT));

            return CouponDetailDto.entityToDto(data);
        } catch (CustomException e) {
            return null;
        }
    }

    /**
     * ADMIN 페이지에서 등록된 쿠폰을 수정할 수 있는 함수
     */

    @Transactional
    public boolean updateCouponDetail(CouponUpdateDto req) {
        CouponBenefitType benefitType   // 혜택 구분 [ 원, % ]
                = fromCode(CouponBenefitType.class, req.getBenefitType());
        CouponProductTargetType productTargetType   // 에코 할인 여부 [일반, 에코/그린, 상관없음]
                = fromCode(CouponProductTargetType.class, req.getProductTargetType());
        CouponPaymentTargetType paymentTargetType   //  지급 대상 [ 모든 회원 , 선착순 ]
                = fromCode(CouponPaymentTargetType.class, req.getPaymentTargetType());
        CouponIssuedTimeType issuedTimeType //  발급 시점 [ 구매 후 1주일 뒤, 즉시발급 ]
                = fromCode(CouponIssuedTimeType.class, req.getIssuedTimeType());
        CouponExpireTimeType expireTimeType //  사용기간 [ 발급잉로 부터 N일, 무제한 ]
                = fromCode(CouponExpireTimeType.class, req.getExpireTimeType());
        CouponCoverageType issueCoverageType //  발급 적용 범위 [ 모든상품/브랜드, 특정 브랜드]
                = fromCode(CouponCoverageType.class, req.getIssueCoverageType());
        CouponCoverageType useCoverageType  //  쿠폰 사용 범위 [ 모든상품/브랜드, 특정 브랜드]
                = fromCode(CouponCoverageType.class, req.getUseCoverageType());
        CouponStandardType useStandardType   //  쿠폰 사용가능 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                = fromCode(CouponStandardType.class, req.getUseStandardType());
        CouponStandardType issueStandardType //  쿠폰 발급 기준 금액 [ 가격제한없음, N원 이상 구매시 ]
                = fromCode(CouponStandardType.class, req.getIssueStandardType());
        CouponPeriodType periodType //  기간 설정 [ 기간내 N 회 이상 주문시 , 지정 기간 없음 (지속적인 기준) ]
                = fromCode(CouponPeriodType.class, req.getPeriodType());
        CouponAutoManualType autoManualType //  자동 / 수동 발급 [ 자동, 수동 ]
                = fromCode(CouponAutoManualType.class, req.getAutoManualType());
        CouponType couponType           // 쿠폰 타입 [ 이벤트, 지급형 ]
                = fromCode(CouponType.class, req.getType());
        ;

        Coupon coupon = couponRepository.findById(req.getId())
                .orElseThrow(() -> new CustomException(CommonErrorType.INVALID_COUPON_FORMAT));

        coupon.setBenefitType(benefitType);
        if (req.getBenefitValue() > 0) {
            coupon.setBenefitValue(req.getBenefitValue());
        } else {
            throw new CustomException(CommonErrorType.INVALID_VALUE);
        }

        coupon.setProductTargetType(productTargetType);

        coupon.setPaymentTargetType(paymentTargetType);
        if (paymentTargetType == CouponPaymentTargetType.FIRST && req.getFirstCount() > 0) {
            coupon.setFirstCount(req.getFirstCount());
        } else {
            coupon.setFirstCount(null);
        }
        coupon.setIssuedTimeType(issuedTimeType);
        coupon.setType(couponType);

        coupon.setExpireTimeType(expireTimeType);
        if (req.getExpireDays() > 0) {
            coupon.setExpireDays(req.getExpireDays());
        } else {
            throw new CustomException(CommonErrorType.INVALID_VALUE);
        }

        //TODO: Temp Fix
        coupon.setIssueCoverageType(issueCoverageType);
        coupon.setUseCoverageType(useCoverageType);

        coupon.setUseStandardType(useStandardType);
        if (useStandardType == CouponStandardType.LIMIT && req.getUseStandardValue() > 0) {
            coupon.setUseStandardValue(req.getUseStandardValue());
        } else {
            coupon.setUseStandardValue(null);
        }

        coupon.setIssueStandardType(issueStandardType);
        if (issueStandardType == CouponStandardType.LIMIT && req.getIssueStandardValue() > 0) {
            coupon.setIssueStandardValue(req.getIssueStandardValue());
        } else {
            coupon.setIssueStandardValue(null);
        }

        coupon.setPeriodType(periodType);
        if (periodType == CouponPeriodType.SET) {
            if (req.getPeriodStartAt() != null && req.getPeriodEndAt() != null && req.getNumberOfPeriod() != null) {
                LocalDate startAt = LocalDate.parse(req.getPeriodStartAt(), DateTimeFormatter.ISO_DATE);
                coupon.setPeriodStartAt(startAt);
                LocalDate endAt = LocalDate.parse(req.getPeriodEndAt(), DateTimeFormatter.ISO_DATE);
                coupon.setPeriodEndAt(endAt);

                coupon.setNumberOfPeriod(req.getNumberOfPeriod());
            }
        } else {
            coupon.setPeriodStartAt(null);
            coupon.setPeriodEndAt(null);
            coupon.setNumberOfPeriod(req.getNumberOfPeriod());
        }

        // 쿠폰코드 수동 입력될 경우
        if (req.getCode() != null) {
            Optional<Coupon> findCode = couponRepository.findByCode(req.getCode());
            if (findCode.isPresent()) {
                throw new CustomException(CommonErrorType.DUPLICATED_COUPON_CODE);
            } else {
                coupon.setCode(req.getCode());
            }
        }

        coupon.setAutoManualType(autoManualType);

        coupon.setLoginAlert(req.getLoginAlert());
        coupon.setSmsAlert(req.getSmsAlert());
        coupon.setEmailAlert(req.getEmailAlert());

        return true;
    }

    /**
     * ADMIN 페이지에서 회원 검색 및 모든 유저에게 쿠폰을 지급 하는 함수
     */
    @Transactional
    public boolean addCouponUser(CouponUserIssuedDto couponUserIssuedDto) {
        Coupon coupon;
        LocalDateTime couponExpireAt = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        if (couponUserIssuedDto.getCouponId() != null) {
            coupon = couponRepository.findById(couponUserIssuedDto.getCouponId())
                    .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_COUPON, CommonErrorType.NOT_EXISTED_COUPON.getMsg()));
        } else {
            return false;
        }

        if (couponUserIssuedDto.getCouponTarget().equals(ProvisionTarget.USER.getCode())) {
            User user = userRepository.findById(couponUserIssuedDto.getUserId())
                    .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_EMAIL));


            Long couponExpireDay = coupon.getExpireDays() + 1; // 23:59분 을 위해
            if (coupon.getExpireDays() < 0) {
                couponExpireAt = null;
            } else {
                couponExpireAt = couponExpireAt.plusDays(couponExpireDay).minusMinutes(1);
            }

            CouponIssuedTimeType issuedTimeType = coupon.getIssuedTimeType();
            boolean couponLock = false;

            if (issuedTimeType == CouponIssuedTimeType.WEEK) {   // 1주일 뒤 발급 따라서 기본적으로 lock, 조건충족시 해제
                couponLock = true;
            }

            CouponUser couponUser = CouponUser.builder()
                    .coupon(coupon)
                    .user(user)
                    .expiredAt(couponExpireAt)
                    .couponLock(couponLock)
                    .build();

            couponUserRepository.save(couponUser);
        } else if (couponUserIssuedDto.getCouponTarget().equals(ProvisionTarget.ALL.getCode())) {
            List<User> userList = userRepository.findAll();

            Long couponExpireDay = coupon.getExpireDays() + 1; // 23:59분 을 위해
            if (couponExpireDay < 0) {
                couponExpireAt = null;
            } else {
                couponExpireAt = couponExpireAt.plusDays(couponExpireDay).minusMinutes(1);
            }
            final LocalDateTime resultCouponExpireAt = couponExpireAt;
            CouponIssuedTimeType issuedTimeType = coupon.getIssuedTimeType();

            boolean couponLock = false;

            if (issuedTimeType == CouponIssuedTimeType.WEEK) {   // 1주일 뒤 발급 따라서 기본적으로 lock, 조건충족시 해제
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
        //TODO: 알림 서비스 (복수로도 가능하게끔 해야할까?)
        couponAlarm(couponUserIssuedDto.getAlarmType());
        return true;
    }

    public void couponAlarm(String[] alarmTypeCode) {
        for (String alarmType : alarmTypeCode) {
            CouponProvideAlarmType couponProvideAlarmType = fromCode(CouponProvideAlarmType.class, alarmType);
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
                case UNKNOWN -> {
                    throw new CustomException(CommonErrorType.INVALID_ALARM);
                }
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


    @Transactional
    public void joinCouponForOpenEvent(Long userId) {
        String openEventCode = CouponUtils.OPEN_EVENT_CODE;

        Optional<Coupon> findEventCode = couponRepository.findByCode(openEventCode);
        if (findEventCode.isPresent()) {
            Coupon coupon = findEventCode.get();
            if (coupon.getStatusType() == CouponStatusType.ISSUED) {
                CouponRegisterDto couponRegisterDto = CouponRegisterDto.builder()
                        .userId(userId)
                        .couponCode(openEventCode)
                        .build();
                couponService.couponRegister(couponRegisterDto);
            }
        }
    }


    private String generateCode() {
        String couponCode = CouponUtils.generateCode();
        Optional<Coupon> findCode = couponRepository.findByCode(couponCode);
        if (findCode.isPresent()) {
            generateCode();
        }
        return couponCode;
    }


}
