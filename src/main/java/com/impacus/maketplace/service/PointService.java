package com.impacus.maketplace.service;

import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.point.PointRequestDto;
import com.impacus.maketplace.dto.point.PointSettingRequestDto;
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

    private final ObjectCopyHelper objectCopyHelper;

    @Transactional
    public boolean addPointMaster(PointSettingRequestDto pointSettingRequestDto) {

        PointMaster pointMaster = objectCopyHelper.copyObject(pointSettingRequestDto, PointMaster.class);
        pointMasterRepository.save(pointMaster);

        PointHistory pointHistory = objectCopyHelper.copyObject(pointSettingRequestDto, PointHistory.class);
        pointHistoryRepository.save(pointHistory);

        //TODO: 변경행햐야함
        return false;

    }


}
