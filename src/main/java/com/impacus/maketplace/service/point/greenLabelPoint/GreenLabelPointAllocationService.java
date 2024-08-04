package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistory;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistoryRelation;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRelationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GreenLabelPointAllocationService {
    private final GreenLabelPointAllocationRepository allocationRepository;
    private final GreenLabelPointRepository greenLabelPointRepository;
    private final GreenLabelPointHistoryRepository historyRepository;
    private final GreenLabelPointHistoryRelationRepository relationRepository;

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
            throw new CustomException(PointErrorType.INVALID_POINT_MANAGE, "지급 포인트는 음수일 수 없습니다.");
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
     *
     * @param userId     그린 라벨 포인트를 차감할 사용자 아이디
     * @param type       포인트 이력 타입 (예: 상품 구매, 상품 환불, 출석 체크 등)
     * @param usedPoints 사용된 포인트
     * @return 포인트 차감 이력 저장된 greenLabelPointHistory의 id
     */
    public Long deductPoints(
            Long userId,
            PointType type,
            Long usedPoints,
            @Nullable boolean lkjkljlksdfs
    ) {
        return null;
    }
}
