package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.point.*;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.LogUtils;
import com.impacus.maketplace.dto.point.CreateGreenLabelHistoryDTO;
import com.impacus.maketplace.dto.point.greenLabelPoint.AppGreenLabelPointDTO;
import com.impacus.maketplace.entity.point.RewardPoint;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistoryRelation;
import com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory.GreenLabelPointHistory;
import com.impacus.maketplace.repository.point.RewardPointRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRelationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointMasterRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointAllocationService {
    private final GreenLabelPointAllocationRepository allocationRepository;
    private final GreenLabelPointRepository greenLabelPointRepository;
    private final GreenLabelPointHistoryService greenLabelPointHistoryService;
    private final GreenLabelPointHistoryRelationRepository relationRepository;
    private final LevelPointMasterRepository levelPointMasterRepository;
    private final RewardPointRepository rewardPointRepository;

    @Transactional
    public boolean payGreenLabelPoint(Long userId, PointType pointType, Long tradePoint) {
        return payGreenLabelPoint(userId, pointType, tradePoint, null);
    }

    /**
     * 그린 라벨 포인트를 지급하는 함수
     *
     * @param userId     포인트 지급받을 사용자 아이디
     * @param pointType  지급 포인트 타입
     * @param tradePoint 지급 포인트
     * @param orderId    주문 아이디 (주문 포인트인 경우에만 필수)
     * @return 포인트 지급 성공 여부
     */
    @Transactional
    public boolean payGreenLabelPoint(
            Long userId,
            PointType pointType,
            Long tradePoint,
            @Nullable Long orderId
    ) {
        try {
            // 1. 지급 포인트 유효성 확인
            if (tradePoint < 0) {
                throw new CustomException(PointErrorType.INVALID_POINT, "지급 포인트는 음수일 수 없습니다.");
            }
            if (!checkIsGreenPointsAwardable(userId, pointType)) {
                return false;
            }

            // 2. 포인트 리워드 타입이 지급 가능한 상태인지 확인
            if (!validateAndIncrementIssueQuantity(pointType.getRewardPointType())) {
                return false;
            }

            // 3. 포인트 지급
            Long greenLabelPoint = greenLabelPointRepository.findWriteLockGreenLablePointByUserId(userId);
            Long changedPoint = greenLabelPoint + tradePoint;
            greenLabelPointRepository.updateGreenLabelPointByUserId(userId, changedPoint);

            // 4. 레벨 포인트 이력 저장
            // GreenLabelPointAllocation, GreenLabelPointHistory, GreenLabelPointHistoryRelation 저장
            GreenLabelPointAllocation allocation = GreenLabelPointAllocation.of(
                    userId,
                    pointType,
                    tradePoint
            );
            allocationRepository.save(allocation);
            CreateGreenLabelHistoryDTO dto = CreateGreenLabelHistoryDTO.of(
                    userId,
                    pointType,
                    PointStatus.GRANT,
                    tradePoint,
                    0L,
                    changedPoint,
                    levelPointMasterRepository.findLevelPointByUserId(userId),
                    orderId
            );
            GreenLabelPointHistory history = greenLabelPointHistoryService.saveHistory(dto);

            GreenLabelPointHistoryRelation relation = GreenLabelPointHistoryRelation.of(
                    allocation.getId(),
                    history.getId(),
                    LocalDateTime.now().plusMonths(6)
            );
            relationRepository.save(relation);

            return true;
        } catch (CustomException ex) {
            throw new CustomException(ex);
        } catch (Exception ex) {
            LogUtils.writeInfoLog("GreenLabelPointAllocationService",
                    String.format("fail to pay point: userId {%d} pointType {%s} tradePoint {%d}",
                            userId, pointType, tradePoint)
            );
            return true;
        }
    }

    /**
     * 포인트 타입이 지급 가능한 상태인지 확인하고, 지급가능한 경우, 지급 수를 올리는 함수
     *
     * @param rewardPointType 리워드 포인트 타입
     * @return 데이터 유효 여부
     */
    @Transactional
    public boolean validateAndIncrementIssueQuantity(RewardPointType rewardPointType) {
        if (rewardPointType == null) {
            throw new CustomException(PointErrorType.INVALID_POINT_TYPE, "포인트 리워드를 할 수 없는 포인트 타입입니다.");
        }

        // 1. 리워드 포인트 조회
        RewardPoint rewardPoint = rewardPointRepository.findByRewardPointType(rewardPointType);

        // 2. 지급 가능한 상태인지 확인
        if (rewardPoint.getStatus() == RewardPointStatus.STOPPED || rewardPoint.isDeleted()) {
            return false;
        }

        // 3. 지급 수 추가
        rewardPoint.incrementIssueQuantity();
        rewardPointRepository.save(rewardPoint);

        return true;
    }

    /**
     * 그린 라벨 포인트를 차감하는 함수
     * - 사용된 포인트 만큼 [그린 라벨 포인트 지급] 데이터들을 사용된 상태로 업데이트하고, 사용된 이력을 새로운 [그린 라벨 포인트 이력] 저장
     *
     * @param userId     그린 라벨 포인트를 차감할 사용자 아이디
     * @param type       포인트 이력 타입 (예: 상품 구매, 상품 환불, 출석 체크 등)
     * @param usedPoints 사용된 포인트 (양수)
     * @param allowsNegativeBalance 포인트가 부족하더라도 차감할지 여부
     * @return 포인트 차감 이력 저장된 greenLabelPointHistory 의 id
     */
    @Transactional
    public Long deductPoints(
            Long userId,
            PointType type,
            Long usedPoints,
            boolean allowsNegativeBalance
    ) {
        if (usedPoints < 0) {
            throw new CustomException(PointErrorType.INVALID_POINT, "사용될 포인트는 음수일 수 없습니다.");
        }

        // 1. 포인트 차감
        Long greenLabelPoint = greenLabelPointRepository.findWriteLockGreenLablePointByUserId(userId);
        Long tradePoint = usedPoints * (-1);
        Long changedPoint = greenLabelPoint + tradePoint;
        if (changedPoint < 0 && !allowsNegativeBalance) {
            throw new CustomException(PointErrorType.INVALID_POINT, "차감할 그린 라벨 포인트가 보유한 포인트보다 작을 수는 없습니다.");
        }
        greenLabelPointRepository.updateGreenLabelPointByUserId(
                userId,
                changedPoint < 0 ? 0 : changedPoint
        );

        // 2. GreenLabelPointHistory 저장
        CreateGreenLabelHistoryDTO dto = CreateGreenLabelHistoryDTO.of(
                userId,
                type,
                PointStatus.USE,
                tradePoint,
                changedPoint < 0 ? changedPoint : 0L,
                changedPoint < 0 ? 0 : changedPoint,
                levelPointMasterRepository.findLevelPointByUserId(userId)
        );
        GreenLabelPointHistory history = greenLabelPointHistoryService.saveHistory(dto);

        // 3. 사용될 그린 라벨 포인트 지급 이력 업데이트
        Long deductPoint = usedPoints;
        List<NotUsedGreenLabelPointAllocationDTO> pointAllocations = allocationRepository.findNotUsedGreenLabelPointByUserId(userId);
        for (NotUsedGreenLabelPointAllocationDTO allocation : pointAllocations) {
            if (deductPoint <= 0) {
                break;
            }
            Long remainPoint = allocation.getRemainPoint();

            PointUsageStatus changedStatus;
            Long changedRemainPoint;
            if (remainPoint > deductPoint) {
                // 남은 포인트가 많은 경우: 차감 포인트 만큼만 차감
                changedStatus = PointUsageStatus.IN_USE;
                changedRemainPoint = remainPoint - deductPoint;
                deductPoint = 0L;
            } else {
                // 같거나 차감해야 하는 포인트가 많은 경우: 남은 포인트 모두 사용
                changedStatus = PointUsageStatus.COMPLETED;
                changedRemainPoint = 0L;
                deductPoint -= remainPoint;
            }

            // 4. GreenLabelPointAllocation 업데이트, GreenLabelPointHistoryRelation 저장
            GreenLabelPointHistoryRelation relation = GreenLabelPointHistoryRelation.of(
                    allocation.getId(),
                    history.getId(),
                    allocation.getExpiredAt()
            );
            relationRepository.save(relation);
            allocationRepository.updateGreenLabelPointAllocationById(
                    allocation.getId(),
                    changedStatus,
                    changedRemainPoint
            );
        }

        return history.getId();
    }

    /**
     * 그린 라벨 포인트 조회 함수 (배타적 락)
     *
     * @param userId 사용자 ID
     * @return 그린 라벨 포인트
     */
    public AppGreenLabelPointDTO getGreenLabelPointInformation(Long userId) {
        try {
            return allocationRepository.findPointInformationByUserId(userId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 그린 라벨 포인트 조회 함수
     *
     * @param userId 사용자 ID
     * @return 그린 라벨 포인트
     */
    public Long getGreenLabelPointAmount(Long userId) {
        return greenLabelPointRepository.findGreenLabelPointByUserId(userId);
    }

    /**
     * 그린 라벨 포인트를 지급 받을 수 있는지 확인하는 함수
     *
     * @param userId 사용자 ID
     * @param pointType 확인할 포인트 타입
     * @return true: 포인트 지급 가능 / false: 포인트 지급 불가능
     */
    private boolean checkIsGreenPointsAwardable(Long userId, PointType pointType) {
        return switch (pointType) {
            case CHECK -> {
                LocalDateTime allocatedPointAt = allocationRepository.findRecentAllocatedPointAtByUserIdAndPointType(userId, pointType);

                // 오늘 00시 이전의 이력인지 확인
                LocalDateTime todayMidnight = LocalDate.now().atStartOfDay();
                yield allocatedPointAt == null || allocatedPointAt.isBefore(todayMidnight);
            }
            case SHARE_PRODUCT -> {
                Long allocatedPointCnt = allocationRepository.findAllocatedPointCntByUserIdAndPointType(userId, pointType);

                // 오늘 2회 이하로 지급 받았는지 확인
                yield allocatedPointCnt == null || allocatedPointCnt < 2;
            }
            case SNS_TAG -> {
                LocalDateTime allocatedPointAt = allocationRepository.findRecentAllocatedPointAtByUserIdAndPointType(userId, pointType);

                // 현재 시간으로 2주 전의 이력인지 확인
                LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);
                yield allocatedPointAt == null || allocatedPointAt.isBefore(twoWeeksAgo);
            }
            default -> true;
        };
    }
}
