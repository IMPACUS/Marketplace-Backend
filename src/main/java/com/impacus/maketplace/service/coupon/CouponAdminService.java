package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.dto.coupon.request.CouponDTO;
import com.impacus.maketplace.dto.coupon.request.CouponEventTypeDTO;
import com.impacus.maketplace.dto.coupon.request.CouponIssueDTO;
import com.impacus.maketplace.dto.coupon.request.CouponUpdateDTO;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.dto.coupon.response.CouponListInfoDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponHistoriesDTO;
import com.impacus.maketplace.dto.coupon.response.IssueCouponInfoDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import com.impacus.maketplace.repository.coupon.querydsl.CouponCustomRepositroy;
import com.impacus.maketplace.repository.seller.SellerRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponAdminService {

    private final SellerRepository sellerRepository;
    private final CouponRepository couponRepository;
    private final CouponCustomRepositroy couponCustomRepositroy;
    private final UserRepository userRepository;
    private final ProvisionCouponService provisionCouponService;

    /**
     * 쿠폰 코드 중복 검사
     *
     * @param code
     */
    public void duplicateCheckCode(String code) {
        if (couponRepository.existsByCode(code)) {
            throw new CustomException(new CustomException(CouponErrorType.DUPLICATED_COUPON_CODE));
        }
    }

    /**
     * 관리자 페이지에서 쿠폰 등록
     *
     * @param couponIssuedDto 쿠폰 발급 DTO
     * @return coupon 저장된 쿠폰 Entity
     */
    @Transactional
    public Coupon registerCoupon(CouponIssueDTO couponIssuedDto) {

        // 1. 쿠폰 등록 규칙 검사
        validateCouponRegistrationRules(couponIssuedDto);

        // 2. 쿠폰 코드 처리
        String code = getCode(couponIssuedDto.getAutoManualType(), couponIssuedDto);

        Coupon coupon = couponIssuedDto.toEntity(code);

        return couponRepository.save(coupon);
    }

    /**
     * 관리자 페이지에서 쿠폰 수정
     *
     * @param couponUpdateDTO 쿠폰 수정 DTO
     * @return coupon 수정된 쿠폰 Entity
     */
    @Transactional
    public Coupon updateCoupon(CouponUpdateDTO couponUpdateDTO) {

        // 1. 해당 id를 가진 쿠폰의 발급 수량 가져오기(Lock)
        Coupon coupon = couponRepository.findWriteLockById(couponUpdateDTO.getCouponId()).orElseThrow(() -> {
            log.error("CouponAdminService.updateCoupon error: id값이 존재하지 않습니다. " +
                    "id: {}", couponUpdateDTO.getCouponId());
            throw new CustomException(CouponErrorType.NOT_EXISTED_COUPON);
        });

        // 2. 삭제된 쿠폰인지 확인
        if (coupon.getIsDeleted()) {
            log.error("CouponAdminService.updateCoupon error: 삭제된 쿠폰입니다. " +
                    "id: {}", couponUpdateDTO.getCouponId());
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        // 3. 발급한 횟수 확인
        if (coupon.getQuantityIssued() > 0) {
            log.error("CouponAdminService.updateCoupon error: 사용자에게 발급한 이력이 있습니다. QuantityIssued: {}", coupon.getQuantityIssued());
            throw new CustomException(CouponErrorType.INVALID_COUPON_UPDATE);
        }

        // 4. 입력 값 검증
        validateCouponRegistrationRules(couponUpdateDTO);

        // 5. 쿠폰 코드 처리
        String code = getCode(couponUpdateDTO.getAutoManualType(), couponUpdateDTO);

        // 6. 쿠폰 업데이트
        coupon.update(code, couponUpdateDTO);

        return coupon;
    }

    /**
     * 단일 쿠폰 조회
     *
     * @param id
     * @return
     */
    public CouponDetailDTO getCoupon(Long id) {

        // 1. id를 통한 Coupon 조회
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        // 2. 삭제된 쿠폰인지 확인
        if (coupon.getIsDeleted()) {
            log.error("CouponAdminService.updateCoupon error: 삭제된 쿠폰입니다. " +
                    "id: {}", id);
            throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
        }

        return CouponDetailDTO.fromEntity(coupon);
    }

    /**
     * 선택한 쿠폰 리스트 상태 변경
     *
     * @param couponIdList
     * @param changeCouponStatus
     * @return
     */
    @Transactional
    public void changeStatus(List<Long> couponIdList, CouponStatusType changeCouponStatus) {

        // 1. id를 통한 쿠폰 조회
        List<Coupon> coupons = couponRepository.findWriteLockCouponsById(couponIdList);

        // 2. 일치하는 쿠폰이 없다면,
        if (coupons.isEmpty()) {
            throw new CustomException(CouponErrorType.NOT_EXISTED_COUPON);
        }

        // 3. Deleted 쿠폰이 있다면 예외를 던지고 아니면 상태 변경
        coupons.forEach(coupon -> {
            if (coupon.getIsDeleted()) {
                throw new CustomException(CouponErrorType.IS_DELETED_COUPON);
            } else {
                coupon.setStatusType(changeCouponStatus);
            }
        });
    }

    /**
     * 쿠폰 삭제하기
     *
     * @param couponIdList
     */
    @Transactional
    public void deleteCoupon(List<Long> couponIdList) {

        // 1. id를 통한 쿠폰 조회
        List<Coupon> coupons = couponRepository.findWriteLockCouponsById(couponIdList);

        // 2. 일치하는 쿠폰이 없다면,
        if (coupons.isEmpty()) {
            throw new CustomException(CouponErrorType.NOT_EXISTED_COUPON);
        }

        // 3. 쿠폰 삭제하기
        coupons.forEach(coupon -> {
            coupon.setStatusType(CouponStatusType.STOP);
            coupon.setIsDeleted(true);
        });
    }


    /**
     * 쿠폰 목록 Pagination
     *
     * @param name         쿠폰명/혜택
     * @param couponStatus 발급 상태
     * @param pageable     페이지 숫자 및 크기
     * @return couponListInfoDTOList
     */
    public Page<CouponListInfoDTO> getCouponListInfoList(String name, CouponStatusType couponStatus, Pageable pageable) {
        return couponCustomRepositroy.findCouponListInfo(name, couponStatus, pageable);
    }

    /**
     * 쿠폰 지급하기 페이지: 쿠폰 정보 조회
     *
     * @return List<PayCouponInfoDTO>
     */
    public List<IssueCouponInfoDTO> getIssueCouponInfoList() {
        return couponCustomRepositroy.findIssueCouponInfoList();
    }

    /**
     * <h3>쿠폰 지급하기 페이지: ADMIN이 기존의 제약 조건 무시하고 조건에 해당하는 모든 유저에게 쿠폰을 발급</h3>
     * <p>사용자 조건</p>
     * <p>1. 파라미터로 들어온 레벨이 null이 아닐 경우 레벨에 맞는 유저 조회</p>
     * <p>2. 파라미터로 들어온 레벨이 null일 경우 모든 유저 조회</p>
     * <p>3. 활성화 상태인 유저</p>
     * <p>4. 인증된 회원</p>
     */
    @Transactional
    public void issueCouponAllUser(Long couponId, UserLevel userLevel) {

        List<Long> userIds = userRepository
                .findUserIdByUserLevel(userLevel);

        // 2. 해당하는 유저들에게 쿠폰 발급
        provisionCouponService.issueCouponToUsersByAdmin(userIds, couponId);
    }


    /**
     * <h3>쿠폰 지급하기 페이지: ADMIN이 기존의 쿠폰 제약 조건 무시하고 특정 유저에게 쿠폰을 발급</h3>
     */
    @Transactional
    public void issueCouponTargetUser(Long userId, Long couponId) {
        // 1. ID를 통해서 회원 검색
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_USER));

        // 1.1 유저 권한 확인
        if (!user.getType().equals(UserType.ROLE_CERTIFIED_USER)) {
            throw new CustomException(CouponErrorType.USER_NOT_AUTHORIZED_FOR_COUPON_EXCEPTION);
        }

        // 2. 쿠폰 지급
        provisionCouponService.issueCouponToUserByAdmin(user.getId(), couponId);
    }


    /**
     * 쿠폰 이름과 쿠폰 발급 상태에 따라 쿠폰 발급 이력 조회 (페이지네이션)
     */
    public IssueCouponHistoriesDTO getIssueCouponHistories(String name, UserCouponStatus userCouponStatus, LocalDate startAt, LocalDate endAt, Pageable pageable) {

        // 1. 입력값 검증(날짜 검증)
        if (startAt != null && endAt != null && startAt.isAfter(endAt)) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA);
        }

        // 2. DB 조회
        return couponCustomRepositroy.findIssueCouponHistories(name, userCouponStatus, startAt, endAt, pageable);
    }

    /**
     * 쿠폰 이벤트 종류 형식 조회
     */
    public List<CouponEventTypeDTO> getCouponEventTypes() {
        return Arrays.stream(EventType.values())
                .map(EventType::convert)
                .collect(Collectors.toList());
    }

    /**
     * 쿠폰 코드를 수동/자동 방식에 따라 중복 검증 후 가져오는 함수
     *
     * @param coupontDto 쿠폰 DTO
     * @return code 검증 완료된 쿠폰 코드
     */
    private String getCode(AutoManualType autoManualType, CouponDTO coupontDto) {
        String code = coupontDto.getCode();

        if (autoManualType.equals(AutoManualType.AUTO)) {
            // 2.1 자동 코드 생성 방식일 경우, 코드 생성
            code = CouponUtils.generateCode();
            // 2.1.1 생성한 코드가 이미 발급한 코드와 중복된 경우, 재생성 반복
            while (couponRepository.existsByCode(code)) {
                code = CouponUtils.generateCode();
            }
        } else {
            // 2.2 수동 코드 생성 방식일 경우, 길이 검증 및 문자 검증 후 중복 검사
            if (code.length() != 10 || !code.matches("[A-Z0-9]{10}")) {
                log.error("CouponAdminService.getCode error: 올바르지 않은 코드가 수동 생성으로 들어왔습니다. code: {}", code);
                throw new CustomException(CouponErrorType.INVALID_INPUT_CODE);
            }
            if (couponRepository.existsByCode(coupontDto.getCode())) {
                log.error("CouponAdminService.getCode error: 중복된 코드가 입력으로 들어왔습니다. code: {}", code);
                throw new CustomException(CouponErrorType.DUPLICATED_COUPON_CODE);
            }
        }
        return code;
    }


    /**
     * 쿠폰 발행 조건 검증 메서드
     *
     * <p>쿠폰이 정상적으로 등록되기 위한 다양한 검증 규칙을 수행합니다.</p>
     * <ul>
     *     <li><b>혜택 금액 검증:</b> BenefitValue가 음수이면 예외 발생</li>
     *     <li><b>혜택 유형 검증:</b> 혜택이 %일 경우 0% 미만 및 100% 초과 불가</li>
     *     <li><b>선착순 쿠폰 검증:</b> 지급 방식이 선착순일 경우 firstCount가 음수이면 예외 발생</li>
     *     <li><b>쿠폰 사용 기간 검증:</b> 발급일로부터 N일일 경우 N이 음수이면 예외 발생</li>
     *     <li><b>지급형 쿠폰 검증:</b> 지속성 발급 형식으로 등록할 수 없음</li>
     *     <li><b>이벤트 쿠폰 검증:</b> 결제 주문 관련 이벤트 쿠폰이 특정 브랜드 적용일 경우 예외 발생</li>
     *     <li><b>발급 적용 범위 검증:</b> 특정 브랜드 적용 시 해당 브랜드가 존재하는지 확인</li>
     *     <li><b>사용 적용 범위 검증:</b> 특정 브랜드 사용 시 해당 브랜드가 존재하는지 확인</li>
     *     <li><b>사용 기준 금액 검증:</b> 설정된 사용 기준 금액이 음수이면 예외 발생</li>
     *     <li><b>발급 기준 금액 검증:</b> 설정된 발급 기준 금액이 음수이면 예외 발생</li>
     *     <li><b>지정 기간 검증:</b>
     *         <ul>
     *             <li>시작 날짜가 현재 날짜보다 이전인지 확인</li>
     *             <li>종료 날짜가 시작 날짜보다 이전인지 확인</li>
     *             <li>기간 내 N회 이상 주문 시의 N 값이 NULL이거나 음수인지 확인</li>
     *         </ul>
     *     </li>
     * </ul>
     *
     * @param coupon 등록하려는 쿠폰 DTO
     * @throws CustomException 유효성 검사에 실패한 경우 예외 발생
     */
    private void validateCouponRegistrationRules(CouponDTO coupon) {
        // 1. BenefitValue 검증
        if (coupon.getBenefitValue() == null || coupon.getBenefitValue() < 0) {
            log.error("Invalid benefitValue: {}", coupon.getBenefitValue());
            throw new CustomException(CouponErrorType.INVALID_INPUT_BENEFIT_VALUE);
        }

        // 2. BenefitType이 퍼센트일 경우 0% 미만 및 100% 초과 불가
        if (coupon.getBenefitType() == BenefitType.PERCENTAGE &&
                (coupon.getBenefitValue() == null || coupon.getBenefitValue() < 0 || coupon.getBenefitValue() > 100)) {
            log.error("Invalid percentage benefitValue: {}", coupon.getBenefitValue());
            throw new CustomException(CouponErrorType.INVALID_INPUT_BENEFIT_VALUE);
        }

        // 3. 선착순 지급 방식일 경우 firstCount 음수 불가
        if (coupon.getPaymentTarget() == PaymentTarget.FIRST &&
                (coupon.getFirstCount() == null || coupon.getFirstCount() < 0)) {
            log.error("Invalid firstCount: {}", coupon.getFirstCount());
            throw new CustomException(CouponErrorType.INVALID_INPUT_FIRST_COUNT);
        }

        // 4. 발급일로부터 N일 사용 기한 설정 시 N일 음수 불가
        if (coupon.getExpireTimeType() == ExpireTimeType.LIMIT &&
                (coupon.getExpireTimeDays() == null || coupon.getExpireTimeDays() < 0)) {
            log.error("Invalid expireTimeDays: {}", coupon.getExpireTimeDays());
            throw new CustomException(CouponErrorType.INVALID_INPUT_EXPIRE_TIME_DAYS);
        }

        // 5. 지급형 쿠폰의 지속적 발급 불가
        if (coupon.getCouponType() == CouponType.PROVISION &&
                coupon.getCouponIssueType().equals(CouponIssueType.PERSISTENCE)) {
            log.error("Provision coupon cannot be persistent.");
            throw new CustomException(CouponErrorType.INVALID_INPUT_PROVISION_COUPON_RULE);
        }

        // 6. 결제 주문 이벤트 쿠폰이 특정 브랜드 적용일 경우 예외 발생
        if (coupon.getCouponType() == CouponType.EVENT &&
                coupon.getEventType() == EventType.PAYMENT_ORDER &&
                coupon.getIssueCoverageType() == CoverageType.BRAND) {
            log.error("Payment order event coupons cannot be brand-specific.");
            throw new CustomException(CouponErrorType.INVALID_INPUT_ISSUE_COVERAGE_TYPE);
        }

        // 7. 특정 브랜드 적용 범위 검증
        if (coupon.getIssueCoverageType() == CoverageType.BRAND &&
                (coupon.getIssueCoverageSubCategoryName() == null ||
                        !sellerRepository.existsByMarketName(coupon.getIssueCoverageSubCategoryName()))) {
            log.error("Invalid issueCoverageSubCategoryName: {}", coupon.getIssueCoverageSubCategoryName());
            throw new CustomException(CouponErrorType.INVALID_INPUT_ISSUE_COVERAGE_SUB_CATEGORY_NAME);
        }


        // 8. 특정 브랜드 사용 범위 검증
        if (coupon.getUseCoverageType() == CoverageType.BRAND &&
                (coupon.getUseCoverageSubCategoryName() == null ||
                        !sellerRepository.existsByMarketName(coupon.getUseCoverageSubCategoryName()))) {
            log.error("Invalid useCoverageSubCategoryName: {}", coupon.getUseCoverageSubCategoryName());
            throw new CustomException(CouponErrorType.INVALID_INPUT_USE_COVERAGE_SUB_CATEGORY_NAME);
        }


        // 9. 사용 기준 금액이 음수인지 확인
        if (coupon.getUseStandardType() == StandardType.LIMIT &&
                (coupon.getUseStandardValue() == null || coupon.getUseStandardValue() < 0)) {
            log.error("Invalid useStandardValue: {}", coupon.getUseStandardValue());
            throw new CustomException(CouponErrorType.INVALID_INPUT_USE_STANDARD_VALUE);
        }

        // 10. 발급 기준 금액이 음수인지 확인
        if (coupon.getIssueConditionType().equals(StandardType.LIMIT) &&
                (coupon.getIssueConditionValue() == null || coupon.getIssueConditionValue() < 0)) {
            log.error("Invalid issueConditionValue: {}", coupon.getIssueConditionValue());
            throw new CustomException(CouponErrorType.INVALID_INPUT_ISSUE_STANDARD_VALUE);
        }

        // 11. 지정 기간 설정 시 검증
        if (coupon.getPeriodType().isSetPeriod()) {
            if (coupon.getPeriodType() == PeriodType.SET) {
                // 11.1 시작 날짜 검증
                if (coupon.getPeriodStartAt() == null || coupon.getPeriodStartAt().isBefore(LocalDate.now())) {
                    log.error("Invalid periodStartAt: {}", coupon.getPeriodStartAt());
                    throw new CustomException(CouponErrorType.INVALID_INPUT_PERIOD_START_AT);
                }
                // 11.2 종료 날짜 검증
                if (coupon.getPeriodEndAt() == null || coupon.getPeriodEndAt().isBefore(coupon.getPeriodStartAt())) {
                    log.error("Invalid periodEndAt: {} must be after periodStartAt: {}", coupon.getPeriodEndAt(), coupon.getPeriodStartAt());
                    throw new CustomException(CouponErrorType.INVALID_INPUT_PERIOD_END_AT);
                }
            }
            // 11.3 기간 내 주문 횟수 검증
            if (coupon.getNumberOfPeriod() == null || coupon.getNumberOfPeriod() < 0) {
                log.error("Invalid numberOfPeriod: {}", coupon.getNumberOfPeriod());
                throw new CustomException(CouponErrorType.INVALID_INPUT_NUMBER_OF_PERIOD);
            }
        }
    }
}
