package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
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
            throw new CustomException(PointErrorType.INVALID_POINT_MANAGE, "지급 포인트는 음수일 수 없습니다.");
        }

        // 2. 포인트 지급 및 소멸일 6개월 연장
        LevelPointMaster levelPointMaster = findLevelPointMasterByUserId(userId);
        Long changedPoint = levelPointMaster.getLevelPoint() + tradePoint;

        // 3. 등급 변동 후, 포인트 지급되어야 하는지 확인
        UserLevel userLevel = levelPointMaster.getUserLevel();
        UserLevel changedLevel = userLevel;
        if (userLevel != UserLevel.ECO_VIP && userLevel.checkIsPossibleUpgrade(changedPoint)) {
            changedLevel = switch (userLevel) {
                case NONE -> UserLevel.BRONZE;
                case BRONZE -> UserLevel.ROOKIE;
                case ROOKIE -> UserLevel.SILVER;
                case SILVER -> UserLevel.GOLD;
                case GOLD -> UserLevel.ECO_VIP;
                default -> throw new CustomException(CommonErrorType.UNKNOWN, "등급 변동을 할 수 없는 레벨입니다.");
            };
            levelAchievementService.upgradeUserLevel(userId, changedLevel);
        }

        // 4. 등급 변동
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
                tradePoint
        );
    }

    /**
     * 레벨 포인트 반환 함수
     *
     * @param userId     포인트 반환받을 사용자 아이디
     * @param pointType  포인트 이력 타입
     * @param tradePoint 반환 포인트
     */
    @Transactional
    public void returnPoint(Long userId, PointType pointType, Long tradePoint) {
        LevelPointMaster levelPointMaster = findLevelPointMasterByUserId(userId);
        // levelPointHistoryId 로 이력 조회

        // 1. 포인트 차감

        // 2. 포인트 만료일 원복
        // (가장 최근의 상품 구매일에서 +6개월)
        // 상품 구매일이 없는 경우, levelpointMaster의 생성일

        // 3. 등급 변동 있는지 확인

        // 3.
    }
}
