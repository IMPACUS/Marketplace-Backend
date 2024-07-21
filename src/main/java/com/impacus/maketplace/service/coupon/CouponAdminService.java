package com.impacus.maketplace.service.coupon;

import com.impacus.maketplace.common.enumType.coupon.*;
import com.impacus.maketplace.common.enumType.error.CouponErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.CouponUtils;
import com.impacus.maketplace.dto.coupon.request.CouponDTO;
import com.impacus.maketplace.dto.coupon.request.CouponIssueDTO;
import com.impacus.maketplace.dto.coupon.request.CouponUpdateDTO;
import com.impacus.maketplace.dto.coupon.response.CouponDetailDTO;
import com.impacus.maketplace.entity.coupon.Coupon;
import com.impacus.maketplace.repository.category.SubCategoryRepository;
import com.impacus.maketplace.repository.category.SuperCategoryRepository;
import com.impacus.maketplace.repository.coupon.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CouponAdminService {

    private final SuperCategoryRepository superCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final CouponRepository couponRepository;
    private final CouponUtils couponUtils;


    /**
     * 관리자 페이지에서 쿠폰 등록
     * @param couponIssuedDto 쿠폰 발급 DTO
     * @return coupon 저장된 쿠폰 Entity
     */
    @Transactional
    public Coupon addCoupon(CouponIssueDTO couponIssuedDto) {

        // 1. 입력 값 검증
        couponInputValidation(couponIssuedDto);

        // 2. 쿠폰 코드 처리
        String code = getCode(couponIssuedDto.getAutoManualType(), couponIssuedDto);

        Coupon coupon = couponIssuedDto.toEntity(code);

        return couponRepository.save(coupon);
    }

    /**
     * 관리자 페이지에서 쿠폰 수정
     * @param couponUpdateDTO 쿠폰 수정 DTO
     * @return coupon 수정된 쿠폰 Entity
     */
    @Transactional
    public Coupon updateCoupon(CouponUpdateDTO couponUpdateDTO) {

        // 1. 해당 id를 가진 쿠폰의 발급 수량 가져오기(Lock)
        Coupon coupon = couponRepository.findWriteLockById(couponUpdateDTO.getId()).orElseThrow(() -> {
            log.error("CouponAdminService.updateCoupon error: id값이 존재하지 않습니다. " +
                    "id: {}",couponUpdateDTO.getId());
            return new CustomException(CouponErrorType.NOT_EXISTED_COUPON);
        });

        // 2. 발급한 횟수 확인
        if (coupon.getQuantityIssued() > 0) {
            log.error("CouponAdminService.updateCoupon error: 사용자에게 발급한 이력이 있습니다. QuantityIssued: {}", coupon.getQuantityIssued());
            throw new CustomException(CouponErrorType.INVALID_COUPON_UPDATE);
        }

        // 3. 입력 값 검증
        couponInputValidation(couponUpdateDTO);

        // 4. 쿠폰 코드 처리
        String code = getCode(couponUpdateDTO.getAutoManualType(), couponUpdateDTO);

        // 5. 쿠폰 업데이트
        coupon.update(code, couponUpdateDTO);

        return coupon;
    }

    /**
     * 쿠폰 코드를 수동/자동 방식에 따라 중복 검증 후 가져오는 함수
     * @param coupontDto 쿠폰 DTO
     * @return code 검증 완료된 쿠폰 코드
     */
    private String getCode(AutoManualType autoManualType, CouponDTO coupontDto) {
        String code = coupontDto.getCode();

        if (autoManualType.equals(AutoManualType.AUTO)) {
            // 2.1 자동 코드 생성 방식일 경우, 코드 생성
            code = couponUtils.generateCode();
            // 2.1.1 생성한 코드가 이미 발급한 코드와 중복된 경우, 재생성 반복
            while (couponRepository.existsByCode(code)) {
                code = couponUtils.generateCode();
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


    private void couponInputValidation(CouponDTO couponDTO) {
        // 2.0 BenefitValue가 음수일 경우
        if (couponDTO.getBenefitValue() == null || couponDTO.getBenefitValue() < 0) {
            log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 benefitValue 값이 들어왔습니다. " +
                    "benefitValue: {}", couponDTO.getBenefitValue());
            throw new CustomException(CouponErrorType.INVALID_INPUT_BENEFIT_VALUE);
        }
        // 2.1 Benefit이 %일 경우, 100% 초과 값은 허용 X
        if (couponDTO.getBenefitType().equals(BenefitType.PERCENTAGE)) {
            if (couponDTO.getBenefitValue() == null || couponDTO.getBenefitValue() > 100) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 benefitValue 값이 들어왔습니다. " +
                        "benefitValue: {}", couponDTO.getBenefitValue());
                throw new CustomException(CouponErrorType.INVALID_INPUT_BENEFIT_VALUE);
            }
        }

        // 2.2 쿠폰 지급 방식이 선착순일 경우 값이 음수 X
        if (couponDTO.getPaymentTarget().equals(PaymentTarget.FIRST)) {
            if (couponDTO.getFirstCount() == null || couponDTO.getFirstCount() < 0) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 firstCount 값이 들어왔습니다. " +
                        "firstCount: {}", couponDTO.getFirstCount());
                throw new CustomException(CouponErrorType.INVALID_INPUT_FIRST_COUNT);
            }
        }

        // 2.3 쿠폰 사용 기간이 발급일로부터 N일일 경우 음수 X
        if (couponDTO.getExpireTimeType().equals(ExpireTimeType.LIMIT)) {
            if (couponDTO.getExpireTimeDays() == null || couponDTO.getExpireTimeDays() < 0) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 expireTimeDays 값이 들어왔습니다. " +
                        "expireTimeDays: {}", couponDTO.getExpireTimeDays());
                throw new CustomException(CouponErrorType.INVALID_INPUT_EXPIRE_TIME_DAYS);
            }
        }

        // 2.4 발급 적용 범위가 특정 브랜드일 경우 등록된 브랜드인지 확인
        if (couponDTO.getIssueCoverageType().equals(CoverageType.BRAND)) {
            // 해당 브랜드 명이 존재하는지 확인
            if (couponDTO.getIssueCoverageSubCategoryName() == null ||
                    !subCategoryRepository.existsByName(couponDTO.getIssueCoverageSubCategoryName())) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 issueConverageSubCategoryName 값이 들어왔습니다. " +
                        "issueConverageSubCategoryName: {}", couponDTO.getIssueCoverageSubCategoryName());
                throw new CustomException(CouponErrorType.INVALID_INPUT_ISSUE_COVERAGE_SUB_CATEGORY_NAME);
            }
        }

        // 2.5 쿠폰 사용 범위가 특정 브랜드일 경우 등록된 브랜드인지 확인
        if (couponDTO.getUseCoverageType().equals(CoverageType.BRAND)) {
            // 해당 브랜드 명이 존재하는지 확인
            if (couponDTO.getUseCoverageSubCategoryName() == null ||
                    !subCategoryRepository.existsByName(couponDTO.getUseCoverageSubCategoryName())) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 useCoverageSubCategoryName 값이 들어왔습니다. " +
                        "useCoverageSubCategoryName: {}", couponDTO.getUseCoverageSubCategoryName());
                throw new CustomException(CouponErrorType.INVALID_INPUT_USE_COVERAGE_SUB_CATEGORY_NAME);
            }
        }

        // 2.6 쿠폰 사용 기준 금액 설정한 경우 음수인지 확인
        if (couponDTO.getUseStandardType().equals(StandardType.LIMIT)) {
            if (couponDTO.getUseStandardValue() == null || couponDTO.getUseStandardValue() < 0) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 useStandardValue 값이 들어왔습니다. " +
                        "useStandardValue: {}", couponDTO.getUseStandardValue());
                throw new CustomException(CouponErrorType.INVALID_INPUT_USE_STANDARD_VALUE);
            }
        }

        // 2.7 쿠폰 발급 기준 금액 설정한 경우 음수인지 확인
        if (couponDTO.getIssueStandardType().equals(StandardType.LIMIT)) {
            if (couponDTO.getIssueStandardValue() == null || couponDTO.getIssueStandardValue() < 0) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 issueStandardValue 값이 들어왔습니다. " +
                        "issueStandardValue: {}", couponDTO.getIssueStandardValue());
                throw new CustomException(CouponErrorType.INVALID_INPUT_ISSUE_STANDARD_VALUE);
            }
        }

        // 2.8 지정 기간 설정시
        if (couponDTO.getPeriodType().equals(PeriodType.SET)) {
            // 2.8.1 시작 날짜가 현재 날짜보다 이전인지 확인
            if (couponDTO.getPeriodStartAt() == null || couponDTO.getPeriodStartAt().isBefore(LocalDate.now())) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 periodStartAt 값이 들어왔습니다. " +
                        "periodStartAt: {}, LocalDate.now(): {}", couponDTO.getPeriodStartAt(), LocalDate.now());
                throw new CustomException(CouponErrorType.INVALID_INPUT_PERIOD_START_AT);
            }
            // 2.8.2 종료 날짜가 시작 날짜보다 이전인 경우
            if (couponDTO.getPeriodEndAt() == null || couponDTO.getPeriodEndAt().isBefore(couponDTO.getPeriodStartAt())) {
                log.error("CouponAdminService.couponInputValidation error: periodStartAt는 periodEndAt보다 이전 혹은 같은 날짜여야 합니다. " +
                        "periodStartAt: {}, periodEndAt: {}", couponDTO.getPeriodStartAt(), couponDTO.getPeriodEndAt());
                throw new CustomException(CouponErrorType.INVALID_INPUT_PERIOD_END_AT);
            }
            // 2.8.3 기간 내 N회 이상 주문 시의 N 값이 NULL 혹은 음수인지 확인
            if (couponDTO.getNumberOfPeriod() == null || couponDTO.getNumberOfPeriod() < 0) {
                log.error("CouponAdminService.couponInputValidation error: 올바르지 않은 numberOfPeriod 값이 들어왔습니다. " +
                        "numberOfPeriod: {}", couponDTO.getNumberOfPeriod());
                throw new CustomException(CouponErrorType.INVALID_INPUT_NUMBER_OF_PERIOD);
            }
        }
    }

    public CouponDetailDTO getCoupon(Long id) {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CustomException(CouponErrorType.NOT_EXISTED_COUPON));

        return CouponDetailDTO.fromEntity(coupon);
    }
}
