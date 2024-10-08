package com.impacus.maketplace.service.point;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.AlarmPointDTO;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPoint;
import com.impacus.maketplace.entity.point.levelPoint.LevelAchievement;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointMaster;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;
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
    private final GreenLabelPointAllocationRepository greenLabelPointAllocationRepository;

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

    /**
     * 포인트 알림을 구성하기 위한 데이터를 반환하는 함수
     *
     * @param greenLabelPointAllocationId 조회하려고 하는 그린 라벨 포인트 지급 ID
     * @return 포인트 알림 데이터
     */
    public AlarmPointDTO findAlarmPointByAllocationId(Long greenLabelPointAllocationId) {
        AlarmPointDTO dto = greenLabelPointAllocationRepository.findAlarmPointByAllocationId(greenLabelPointAllocationId);

        if (dto == null) {
            throw new CustomException(PointErrorType.NOT_EXISTED_GREEN_LABEL_POINT_ALLOCATION_ID);
        }

        return dto;
    }
}
