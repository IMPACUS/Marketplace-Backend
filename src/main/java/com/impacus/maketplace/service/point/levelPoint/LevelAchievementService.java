package com.impacus.maketplace.service.point.levelPoint;

import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.point.levelPoint.LevelAchievement;
import com.impacus.maketplace.repository.point.levelPoint.LevelAchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LevelAchievementService {
    private final LevelAchievementRepository levelAchievementRepository;

    /**
     * 사용자 레벨 업그레이드
     * - ECO_VIP 이거나 등급에 처음 달성하였다면 포인트 지급
     *
     * @param userId
     * @param userLevel
     */
    @Transactional
    public void upgradeUserLevel(Long userId, UserLevel userLevel) {
        LevelAchievement levelAchievement = findLevelAchievementByUserId(userId);
        // 1. 처음 달성인지 확인
        // 2. 포인트 지급
        // 3. 사용자 레벨 달성 상태 업데이트
        switch (userLevel) {
            case ROOKIE: {
                if (levelAchievement.isAchievedRookie()) {
                    // TODO 포인트 발급 후, 이력 저장되도록 추가
                }

                levelAchievement.updateRookie(true);
            }
            break;
            case SILVER: {
                if (levelAchievement.isAchievedSilver()) {
                    // TODO 포인트 발급 후, 이력 저장되도록 추가
                }

                levelAchievement.updateSilver(true);
            }
            break;
            case GOLD: {
                if (levelAchievement.isAchievedGold()) {
                    // TODO 포인트 발급 후, 이력 저장되도록 추가
                }

                levelAchievement.updateGold(true);
            }
            break;
            case ECO_VIP: {
                // TODO 포인트 발급 후, 이력 저장되도록 추가
                levelAchievement.updateVip(true);
            }
            break;
        }

        levelAchievementRepository.save(levelAchievement);
    }

    private LevelAchievement findLevelAchievementByUserId(Long userId) {
        return levelAchievementRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_USER));
    }
}
