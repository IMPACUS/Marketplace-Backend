package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.dto.point.PointRequestDto;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.point.PointHistory;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.repository.PointHistoryRepository;
import com.impacus.maketplace.repository.PointMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .userId(userDTO.id())
                .pointMasterId(pointMaster.getId())
                .pointType(PointType.JOIN)
                .changePoint(CELEBRATION_POINT)
                .isManual(false)
                .build();
        pointHistoryRepository.save(pointHistory);

        return false;
    }

    @Transactional
    public void addPoint(PointRequestDto pointRequestDto) {
        PointHistory.builder()
                        .changePoint(pointRequestDto.getSavePoint())
                                .
        pointHistoryRepository.save()
    }
}
