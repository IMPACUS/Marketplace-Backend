package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointHistory;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param hasReceivedLevelUpPoints
     */
    public void saveLevelPointHistory(
            Long userId,
            PointType pointType,
            PointStatus pointStatus,
            Long tradePoint,
            boolean hasReceivedLevelUpPoints
    ) {
        LevelPointHistory levelPointHistory = LevelPointHistory.toEntity(userId, pointType, pointStatus, tradePoint, hasReceivedLevelUpPoints);
        levelPointHistoryRepository.save(levelPointHistory);
    }

    /**
     * 레벨 포인트 이력 아이디로 레벨 포인트 이력 조회하는 함수
     *
     * @param levelPointHistoryId 조회하고자하는 레벨 포인트 이력 아이디
     * @return
     */
    public LevelPointHistory findLevelPointHistoryById(Long levelPointHistoryId) {
        return levelPointHistoryRepository.findById(levelPointHistoryId)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_USER));
    }
}
