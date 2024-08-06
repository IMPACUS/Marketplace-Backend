package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointHistory;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointMaster;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointMasterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("[비즈니스 로직] - 라벨 포인트")
class LevelPointMasterServiceTest {

    @InjectMocks
    private LevelPointMasterService levelPointMasterService;
    @Mock
    private LevelPointMasterRepository levelPointMasterRepository;
    @Mock
    private LevelAchievementService levelAchievementService;
    @Mock
    private LevelPointHistoryService levelPointHistoryService;
    @Mock
    private LevelPointMaster levelPointMaster;
    @Mock
    private LevelPointHistory levelPointHistory;


    @Test
    @DisplayName("[정상 케이스] 레벨 포인트를 지급하고, 등급은 상승되지 않는다.")
    void testPayLevelPoint_success_no_upgrade() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.PURCHASE_GENERAL_PRODUCT;
        Long tradePoint = 100L;
        LevelPointHistory history = LevelPointHistory.toEntity(
                userId,
                pointType,
                PointStatus.USE,
                tradePoint,
                false
        );

        // when
        when(levelPointMasterRepository.findByUserIdForUpdate(userId)).thenReturn(Optional.of(levelPointMaster));
        when(levelPointMaster.getLevelPoint()).thenReturn((long) UserLevel.SILVER.getMinScore());
        when(levelPointMaster.getUserLevel()).thenReturn(UserLevel.SILVER);
        when(levelPointHistoryService.saveLevelPointHistory(userId, pointType, PointStatus.GRANT, tradePoint, false))
                .thenReturn(history.getId());

        Long historyId = levelPointMasterService.payLevelPoint(userId, pointType, tradePoint);

        // then
        //verify(levelPointMasterRepository).updateLevelPointAndExpiredAt((long) UserLevel.SILVER.getMinScore() + tradePoint, UserLevel.SILVER, any(LocalDateTime.class), userId);
        verify(levelPointHistoryService).saveLevelPointHistory(userId, pointType, PointStatus.GRANT, tradePoint, false);
    }

    @Test
    @DisplayName("[정상 케이스] 레벨 포인트를 지급하고, 등급은 상승한다.")
    void testPayLevelPoint_success_with_upgrade() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.PURCHASE_GENERAL_PRODUCT;
        Long tradePoint = 100L;
        LevelPointHistory history = LevelPointHistory.toEntity(
                userId,
                pointType,
                PointStatus.USE,
                tradePoint,
                true
        );

        // when
        when(levelPointMasterRepository.findByUserIdForUpdate(userId)).thenReturn(Optional.of(levelPointMaster));
        when(levelPointMaster.getLevelPoint()).thenReturn((long) UserLevel.SILVER.getMaxScore());
        when(levelPointMaster.getUserLevel()).thenReturn(UserLevel.SILVER);
        when(levelAchievementService.upgradeUserLevelAndAwardPoints(userId, UserLevel.GOLD)).thenReturn(true);

        when(levelPointHistoryService.saveLevelPointHistory(userId, pointType, PointStatus.GRANT, tradePoint, true))
                .thenReturn(history.getId());

        Long historyId = levelPointMasterService.payLevelPoint(userId, pointType, tradePoint);

        // then
        //verify(levelPointMasterRepository).updateLevelPointAndExpiredAt((long) UserLevel.SILVER.getMaxScore() + tradePoint, UserLevel.GOLD, any(LocalDateTime.class), userId);
        verify(levelPointHistoryService).saveLevelPointHistory(userId, pointType, PointStatus.GRANT, tradePoint, true);
    }


    @Test
    @DisplayName("[정상 케이스] 레벨 포인트를 원복한다.")
    void testReturnPoint_success_with_recent_order() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.REFUND_PRODUCT;
        Long levelPointHistoryId = 1L;
        LocalDateTime recentOrderAt = LocalDateTime.now().minusDays(1);
        Long tradeAmount = 100L;

        // when
        when(levelPointMasterRepository.findByUserIdForUpdate(userId)).thenReturn(Optional.of(levelPointMaster));
        when(levelPointHistoryService.findLevelPointHistoryById(levelPointHistoryId)).thenReturn(levelPointHistory);
        when(levelPointMaster.getUserLevel()).thenReturn(UserLevel.SILVER);
        when(levelPointHistory.getTradeAmount()).thenReturn(tradeAmount);
        when(levelPointMaster.getLevelPoint()).thenReturn(UserLevel.SILVER.getMinScore() + tradeAmount);

        levelPointMasterService.returnPoint(userId, pointType, levelPointHistoryId, recentOrderAt);

        // then
        //verify(levelPointMasterRepository).updateLevelPointAndExpiredAt(400L, UserLevel.SILVER, recentOrderAt.plusMonths(6), userId);
        verify(levelPointHistoryService).saveLevelPointHistory(userId, pointType, PointStatus.RETURN, tradeAmount * (-1), false);
    }
}