package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.greenLabelPoint.GreenLabelPointDTO;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistory;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistoryRelation;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRelationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointAllocationService {
    private final GreenLabelPointAllocationRepository allocationRepository;
    private final GreenLabelPointRepository greenLabelPointRepository;
    private final GreenLabelPointHistoryRepository historyRepository;
    private final GreenLabelPointHistoryRelationRepository relationRepository;
    private final GreenLabelPointAllocationRepository greenLabelPointAllocationRepository;

    /**
     * 그린 라벨 포인트를 지급하는 함수
     *
     * @param userId     포인트 지급받을 사용자 아이디
     * @param pointType  지급 포인트 타입
     * @param tradePoint 지급 포인트
     */
    @Transactional
    public void payGreenLabelPoint(Long userId, PointType pointType, Long tradePoint) {
        // 1. 지급 포인트 유효성 확인
        if (tradePoint < 0) {
            throw new CustomException(PointErrorType.INVALID_POINT, "지급 포인트는 음수일 수 없습니다.");
        }

        // 2. 포인트 지급
        Long greenLabelPoint = greenLabelPointRepository.findGreenLabelPointByUserId(userId);
        Long changedPoint = greenLabelPoint + tradePoint;
        greenLabelPointRepository.updateGreenLabelPointByUserId(userId, changedPoint);

        // 4. 레벨 포인트 이력 저장
        // GreenLabelPointAllocation, GreenLabelPointHistory, GreenLabelPointHistoryRelation 저장
        GreenLabelPointAllocation allocation = GreenLabelPointAllocation.of(
                userId,
                pointType,
                tradePoint
        );
        GreenLabelPointHistory history = GreenLabelPointHistory.of(
                userId,
                pointType,
                PointStatus.GRANT,
                tradePoint,
                0L
        );
        allocationRepository.save(allocation);
        historyRepository.save(history);

        GreenLabelPointHistoryRelation relation = GreenLabelPointHistoryRelation.of(
                allocation.getId(),
                history.getId(),
                LocalDateTime.now().plusMonths(6)
        );
        relationRepository.save(relation);
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
        Long greenLabelPoint = greenLabelPointRepository.findGreenLabelPointByUserId(userId);
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
        GreenLabelPointHistory history = GreenLabelPointHistory.of(
                userId,
                type,
                PointStatus.USE,
                tradePoint,
                changedPoint < 0 ? changedPoint : 0L
        );
        historyRepository.save(history);

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
            greenLabelPointAllocationRepository.updateGreenLabelPointAllocationById(
                    allocation.getId(),
                    changedStatus,
                    changedRemainPoint
            );
        }

        return history.getId();
    }

    /**
     * 그린 라벨 포인트 조회 함수
     *
     * @param userId
     * @return
     */
    public GreenLabelPointDTO getGreenLabelPointInformation(Long userId) {
        try {
            return greenLabelPointAllocationRepository.findPointInformationByUserId(userId);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
