package com.impacus.maketplace.service.point;

import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPoint;
import com.impacus.maketplace.entity.point.levelPoint.LevelAchievement;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointMaster;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import com.impacus.maketplace.repository.point.levelPoint.LevelAchievementRepository;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {
    private final LevelPointMasterRepository levelPointMasterRepository;
    private final LevelAchievementRepository levelAchievementRepository;
    private final GreenLabelPointRepository greenLabelPointRepository;

    /**
     * 소비자 생성된 경우, 포인트 관련 엔티티를 생성하는 함수
     * (LevelPointMaster, LevelAchievement, GreenLabelPoint)
     *
     * @param userId 포인트를 연결할 소비자
     */
    @Transactional
    public void addEntityAboutPoint(Long userId) {
        // 1. 엔티티 생성
        LevelPointMaster levelPointMaster = LevelPointMaster.toEntity(userId);
        LevelAchievement levelAchievement = LevelAchievement.toEntity(userId);
        GreenLabelPoint greenLabelPoint = GreenLabelPoint.toEntity(userId);

        // 2. 저장
        levelPointMasterRepository.save(levelPointMaster);
        levelAchievementRepository.save(levelAchievement);
        greenLabelPointRepository.save(greenLabelPoint);
    }
}
