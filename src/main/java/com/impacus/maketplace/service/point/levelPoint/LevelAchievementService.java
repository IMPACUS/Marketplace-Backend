package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.point.levelPoint.LevelAchievement;
import com.impacus.maketplace.repository.point.levelPoint.LevelAchievementRepository;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelAchievementService {
    private final LevelAchievementRepository levelAchievementRepository;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;

    /**
     * 사용자 레벨 업그레이드하고, ECO_VIP 이거나 등급에 처음 달성하였다면 포인트 지급하는 함수
     *
     * @param userId 사용자 ID
     * @param userLevel 변경될 사용자 레벨
     * @return 달성 포인트 지급 여부
     */
    @Transactional
    public boolean upgradeUserLevelAndAwardPoints(Long userId, UserLevel userLevel) {
        LevelAchievement levelAchievement = findLevelAchievementByUserId(userId);

        // 1. 처음 달성인지 확인
        boolean hasReceivedLevelUpPoints = false;
        if (checkIsFirstAchievement(levelAchievement, userLevel) || userLevel == UserLevel.ECO_VIP) {
            // 2. 달성 그린 라벨 포인트 지급
            awardPointsAndLogHistory(userId, userLevel);
            hasReceivedLevelUpPoints = true;
        }

        // 3. 사용자 레벨 달성 상태 업데이트
        updateUserLevelAchievementInUpgrade(levelAchievement, userLevel);
        levelAchievementRepository.save(levelAchievement);
        return hasReceivedLevelUpPoints;
    }

    /**
     * 해당 레벨이 처음 달성되었는지 확인하는 함수
     *
     * @param levelAchievement 사용자 레벨 달성 상태
     * @param userLevel        사용자 레벨
     * @return 처음 달성 여부
     */
    private boolean checkIsFirstAchievement(LevelAchievement levelAchievement, UserLevel userLevel) {
        switch (userLevel) {
            case ROOKIE:
                return !levelAchievement.isAchievedRookie();
            case SILVER:
                return !levelAchievement.isAchievedSilver();
            case GOLD:
                return !levelAchievement.isAchievedGold();
            case ECO_VIP:
                return true;
            default:
                return false;
        }
    }

    /**
     * 달성 포인트 발급 후 이력 저장 함수
     *
     * @param userId    사용자 ID
     * @param userLevel 달성한 사용자 레벨
     */
    private void awardPointsAndLogHistory(Long userId, UserLevel userLevel) {
        greenLabelPointAllocationService.payGreenLabelPoint(
                userId,
                PointType.UPGRADE_LEVEL,
                userLevel.getCelebrationPoint()
        );
    }

    /**
     * (레벨 상승) 사용자 레벨 달성 상태 업데이트
     *
     * @param levelAchievement 사용자 레벨 달성 상태
     * @param userLevel        사용자 레벨
     */
    private void updateUserLevelAchievementInUpgrade(LevelAchievement levelAchievement, UserLevel userLevel) {
        switch (userLevel) {
            case ROOKIE:
                levelAchievement.updateRookie(true);
                break;
            case SILVER:
                levelAchievement.updateSilver(true);
                break;
            case GOLD:
                levelAchievement.updateGold(true);
                break;
            case ECO_VIP:
                levelAchievement.updateVip(true);
                break;
        }
    }

    /**
     * (레벨 하락) 사용자 레벨 달성 상태 업데이트
     *
     * @param levelAchievement 사용자 레벨 달성 상태
     * @param userLevel        하락시키기 전, 사용자 레벨
     */
    private void updateUserLevelAchievementInDowngrade(LevelAchievement levelAchievement, UserLevel userLevel) {
        switch (userLevel) {
            case ROOKIE:
                levelAchievement.updateRookie(false);
                break;
            case SILVER:
                levelAchievement.updateSilver(false);
                break;
            case GOLD:
                levelAchievement.updateGold(false);
                break;
            case ECO_VIP:
                levelAchievement.updateVip(false);
                break;
        }
    }

    /**
     * userId로 LevelAchievement 조회하는 함수
     *
     * @param userId
     * @return
     */
    private LevelAchievement findLevelAchievementByUserId(Long userId) {
        return levelAchievementRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_USER));
    }

    /**
     * 사용자 레벨을 하락시키고, 지급 받았던 달성 포인트를 반환하는 함수
     *
     * @param userId       사용자 ID
     * @param currentLevel 현재 사용자 레벨 (하락하기 전, 레벨)
     * @return 달성 포인트 지급 여부
     */
    public void downgradeUserLevelAndAwardPoints(Long userId, UserLevel currentLevel) {
        LevelAchievement levelAchievement = findLevelAchievementByUserId(userId);

        // 1. 달성 그린 라벨 포인트 반환
        greenLabelPointAllocationService.deductPoints(
                userId,
                PointType.DOWNGRADE_LEVEL,
                currentLevel.getCelebrationPoint(),
                true
        );

        // 2. 사용자 레벨 달성 상태 원복
        updateUserLevelAchievementInDowngrade(levelAchievement, currentLevel);
        levelAchievementRepository.save(levelAchievement);
    }
}
