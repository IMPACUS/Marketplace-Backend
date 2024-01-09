package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.point.PointHistory;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.repository.PointHistoryRepository;
import com.impacus.maketplace.repository.PointMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {

    private final PointMasterRepository pointMasterRepository;
    private final PointHistoryRepository pointHistoryRepository;

    private final Integer CELEBRATION_POINT = 300;

    @Transactional
    public boolean initPointMaster(UserDTO userDTO) {
        if (pointMasterRepository.existsPointMasterByUserId(userDTO.id())) {
            return true;
        }

        PointMaster pointMaster = PointMaster.builder()
                .userId(userDTO.id())
                .availablePoint(CELEBRATION_POINT)
                .userScore(CELEBRATION_POINT)
                .registerId("ADMIN")
                .build();
        pointMasterRepository.save(pointMaster);

        PointHistory pointHistory = PointHistory.builder()
                .pointMasterId(pointMaster.getId())
                .pointType(PointType.JOIN)
                .changePoint(CELEBRATION_POINT)
                .isManual(null)
                .expiredAt(LocalDateTime.now().plusMonths(6L))
                .build();
        pointHistoryRepository.save(pointHistory);

        return false;
    }

    @Transactional
    public PointMasterDto changePoint(PointRequestDto pointRequestDto) {
        PointMaster pointMaster = pointMasterRepository.findByUserId(pointRequestDto.getUserId()).orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER));
        LocalDateTime settingExpiredAt = null;
        if (pointRequestDto.getPointTypeEnum() != PointType.USE &&
                pointRequestDto.getPointTypeEnum() != PointType.EXPIRE) {
            settingExpiredAt = LocalDateTime.now().plusMonths(6L);
        }
        PointHistory pointHistory = PointHistory.builder()
                .changePoint(pointRequestDto.getSavePoint())
                .pointMasterId(pointMaster.getId())
                .pointType(pointRequestDto.getPointTypeEnum())
                .expiredAt(settingExpiredAt)
                .build();
        pointHistoryRepository.save(pointHistory);


        Integer availablePoint = pointMaster.getAvailablePoint();
        Integer userScore = pointMaster.getUserScore();
        Integer savePoint = pointRequestDto.getSavePoint();
        UserLevel currentUserLevel = pointMaster.getUserLevel();

        switch (pointHistory.getPointType()) {
            case USE -> {
                pointMaster.setAvailablePoint(availablePoint - savePoint);
            }
            case SAVE -> {
                pointMaster.setAvailablePoint(availablePoint + savePoint);
                pointMaster.setUserScore(userScore + savePoint);
                userScore += savePoint;

            }
        }

        UserLevel changeUserLevel = UserLevel.fromScore(userScore);
        if (!StringUtils.equals(changeUserLevel, currentUserLevel)) {
            pointMaster.setUserLevel(changeUserLevel);
        }

        PointMasterDto pointMasterDto = new PointMasterDto(pointMaster);
        return pointMasterDto;
    }

    public List<PointHistoryDto> findPointHistory(PointHistorySearchDto pointHistorySearchDto) {
//        PointMaster pointMaster = pointMasterRepository.findByUserId(pointHistorySearchDto.getUserId())
//                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER));
//
//        List<PointHistory> userPointHistoryList = pointHistoryRepository.findByPointMasterId(pointMaster.getId());
//
//        List<PointHistoryDto> resultList = userPointHistoryList.stream()
//                .sorted(Comparator.comparing(PointHistory::getCreateAt))
//                .map(PointHistoryDto::new)
//                .collect(Collectors.toList());
        return pointHistoryRepository.findAllPointHistory(pointHistorySearchDto);
    }
}
