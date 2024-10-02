package com.impacus.maketplace.service.point;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.IssuePointDTO;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPoint;
import com.impacus.maketplace.entity.point.levelPoint.LevelAchievement;
import com.impacus.maketplace.entity.point.levelPoint.LevelPointMaster;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import com.impacus.maketplace.repository.point.levelPoint.LevelAchievementRepository;
import com.impacus.maketplace.repository.point.levelPoint.LevelPointMasterRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import com.impacus.maketplace.service.point.levelPoint.LevelPointMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {
    private final LevelPointMasterRepository levelPointMasterRepository;
    private final LevelAchievementRepository levelAchievementRepository;
    private final GreenLabelPointRepository greenLabelPointRepository;
    private final UserRepository userRepository;
    private final LevelPointMasterService levelPointMasterService;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;

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
     * (모든 회원) 포인트 지급/수취 함수
     * - userLevel 중, ACTIVE 상태인 회원에게 지급/수취
     *
     * @param dto
     */
    @Transactional
    public void issueUserRewardForAllUsers(IssuePointDTO dto) {
        try {
            // 1. userLevel 에 따라서 필터링
            List<Long> userIds = userRepository.findUserIdByUserLevel(dto.getUserLevel());

            // 2. 포인트 지급/수취
            for (Long userId : userIds) {
                issueUserRewardByUserId(userId, dto);
            }

        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * (특정 회원) 포인트 지급/수취 함수
     *
     * @param dto
     */
    @Transactional
    public void issueUserRewardForUser(IssuePointDTO dto) {
//        try {
        //1. userId 찾기
        String email = dto.getEmail();
        if (email == null) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "email은 null일 수 없습니다.");
        }

        // 2. 포인트 지급/수취
        List<Long> userIds = userRepository.findIdByEmailLike("%_" + dto.getEmail());
        if (userIds.isEmpty()) {
            throw new CustomException(CommonErrorType.NOT_EXISTED_EMAIL);
        }
        issueUserRewardByUserId(userIds.get(0), dto);
//        } catch (Exception ex) {
//            throw new CustomException(ex);
//        }
    }

    @Transactional
    public void issueUserRewardByUserId(Long userId, IssuePointDTO dto) {
        PointStatus pointType = dto.getPointStatus();
        if (!Arrays.asList(PointStatus.USE, PointStatus.GRANT).contains(pointType)) {
            throw new CustomException(CommonErrorType.INVALID_REQUEST_DATA, "pointType에 존재하지 않는 값이 요청되었습니다. " + pointType.name());
        }

        // 레벨 포인트 지급/수취
        Long levelPoint = dto.getLevelPoint();
        if (levelPoint != null) {
            if (pointType == PointStatus.GRANT) {
                levelPointMasterService.payLevelPoint(userId, PointType.ADMIN_PROVIDE, levelPoint);
            } else {
                levelPointMasterService.deductLevelPoint(userId, PointType.ADMIN_RECEIVE, levelPoint);
            }
        }

        // 그린 라벨 포인트 지급 수취
        Long greenLabelPoint = dto.getGreenLabelPoint();
        if (greenLabelPoint != null) {
            if (pointType == PointStatus.GRANT) {
                greenLabelPointAllocationService.payGreenLabelPoint(userId, PointType.ADMIN_PROVIDE, greenLabelPoint);
            } else {
                greenLabelPointAllocationService.deductPoints(userId, PointType.ADMIN_RECEIVE, greenLabelPoint, true);
            }
        }
    }
}
