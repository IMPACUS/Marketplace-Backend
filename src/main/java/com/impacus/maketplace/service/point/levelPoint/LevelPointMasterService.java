package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.levelPoint.LevelPointDTO;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointHistory;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointMaster;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelPointMasterService {
    private final LevelPointMasterRepository levelPointMasterRepository;
    private final LevelAchievementService levelAchievementService;
    private final LevelPointHistoryService levelPointHistoryService;

    /**
     * userId로 LevelPointMaster를 조회하는 함수
     *
     * @param userId
     * @return
     */
    public LevelPointMaster findLevelPointMasterByUserId(Long userId) {
        return levelPointMasterRepository.findByUserIdForUpdate(userId)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_USER));
    }


    // 등급에 대한 사용자들을 반환하는 함수

    /**
     * 레벨 포인트를 지급하는 함수
     *
     * @param userId     포인트 지급받을 사용자 아이디
     * @param pointType  지급 포인트 타입
     * @param tradePoint 지급 포인트
     */
    @Transactional
    public void payLevelPoint(Long userId, PointType pointType, Long tradePoint) {
        // 1. 지급 포인트 유효성 확인
        if (tradePoint < 0) {
            throw new CustomException(PointErrorType.INVALID_POINT, "지급 포인트는 음수일 수 없습니다.");
        }

        // 2. 포인트 지급 및 소멸일 6개월 연장
        LevelPointMaster levelPointMaster = findLevelPointMasterByUserId(userId);
        Long changedPoint = levelPointMaster.getLevelPoint() + tradePoint;

        // 3. 등급 변동이 존재하는지 확인
        UserLevel userLevel = levelPointMaster.getUserLevel();
        UserLevel changedLevel = userLevel;
        boolean hasReceivedLevelUpPoints = false;
        if (userLevel != UserLevel.ECO_VIP && userLevel.checkIsPossibleUpgrade(changedPoint)) {
            // 3-1. 등급 변동 후, 포인트 지급이 필요하다면 달성 포인트 지급
            changedLevel = UserLevel.getUpgradeLevel(userLevel);
            hasReceivedLevelUpPoints = levelAchievementService.upgradeUserLevelAndAwardPoints(userId, changedLevel);
        }

        // 4. 변경된 레벨 포인트, 등급 저장
        levelPointMasterRepository.updateLevelPointAndExpiredAt(
                changedPoint,
                changedLevel,
                LocalDateTime.now().plusMonths(6),
                userId
        );

        // 5. 레벨 포인트 이력 저장
        levelPointHistoryService.saveLevelPointHistory(
                userId,
                pointType,
                PointStatus.GRANT,
                tradePoint,
                hasReceivedLevelUpPoints
        );
    }

    /**
     * 레벨 포인트 반환 함수
     *
     * @param userId     포인트 반환받을 사용자 아이디
     * @param pointType  포인트 이력 타입
     * @param levelPointHistoryId 반환하고자 하는 레벨 포인트 이력 id
     * @param recentOrderAt 사용자의 최근 주문이력 날짜 (최근 주문 이력이 없는 경우 null)
     */
    @Transactional
    public void returnPoint(
            Long userId,
            PointType pointType,
            Long levelPointHistoryId,
            LocalDateTime recentOrderAt
    ) {
        LevelPointMaster levelPointMaster = findLevelPointMasterByUserId(userId);
        LevelPointHistory levelPointHistory = levelPointHistoryService.findLevelPointHistoryById(levelPointHistoryId);

        // 1. 포인트 차감
        Long tradePoint = levelPointHistory.getTradeAmount() * (-1);
        Long changedPoint = levelPointMaster.getLevelPoint() + tradePoint;

        // 2. 포인트 만료일 원복
        // 주문이력이 있는 경우: (가장 최근의 상품 구매일에서 +6개월)
        // 주문이력이 없는 경우: levelpointMaster 의 생성일
        LocalDateTime changedExpiredAt = null;
        if (recentOrderAt == null) {
            changedExpiredAt = levelPointMaster.getCreateAt();
        } else {
            changedExpiredAt = recentOrderAt.plusMonths(6);
        }

        // 3. 등급 변동이 존재하는지 확인
        UserLevel userLevel = levelPointMaster.getUserLevel();
        UserLevel changedLevel = userLevel;
        if (userLevel != UserLevel.BRONZE && userLevel.checkIsPossibleDowngrade(changedPoint)) {
            changedLevel = UserLevel.getDowngradeLevel(userLevel);

            // 3-1 등급 상승으로 인해 그린 라벨 포인트를 경우, 달성 그린 라벨 포인트 반환 & 레벨 달성 상태 복구
            if (levelPointHistory.isHasReceivedLevelUpPoints()) {
                levelAchievementService.downgradeUserLevelAndAwardPoints(userId, userLevel);
            }
        }

        // 4. 변경된 레벨 포인트, 등급 저장
        levelPointMasterRepository.updateLevelPointAndExpiredAt(
                changedPoint < 0 ? 0L : changedPoint,
                changedLevel,
                changedExpiredAt,
                userId
        );

        // 5. 레벨 포인트 이력 저장
        levelPointHistoryService.saveLevelPointHistory(
                userId,
                pointType,
                PointStatus.RETURN,
                tradePoint,
                false
        );
    }

    /**
     * 사용자의 레벨 포인트 정보 조회하는 함수
     *
     * @param userId
     * @return
     */
    public LevelPointDTO getLevelPointInformation(Long userId) {
        try {
            return null;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
