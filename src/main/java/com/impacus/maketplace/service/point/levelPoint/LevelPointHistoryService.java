package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointHistory;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelPointHistoryService {
    private final LevelPointHistoryRepository levelPointHistoryRepository;

    /**
     * 레벨 포인트 이력 저장
     *
     * @param userId
     * @param pointType
     * @param pointStatus
     * @param tradePoint
     * @param previousExpirationStartAt
     */
    public void saveLevelPointHistory(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradePoint,
            LocalDateTime previousExpirationStartAt
    ) {
        LevelPointHistory levelPointHistory = LevelPointHistory.toEntity(userId, pointType, pointStatus, tradePoint, previousExpirationStartAt);
        levelPointHistoryRepository.save(levelPointHistory);
    }
}
