package com.impacus.maketplace.service;

import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.point.PointSettingRequestDto;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.repository.PointHistoryRepository;
import com.impacus.maketplace.repository.PointMasterRepository;
import com.impacus.maketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointMasterRepository pointMasterRepository;

    @Autowired
    private PointHistoryRepository pointHistoryRepository;

    @Autowired
    private ObjectCopyHelper objectCopyHelper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void addPointMaster() {


        PointSettingRequestDto pointSettingRequestDto = PointSettingRequestDto.builder()
                .userId(5L)
                .celebrationPoint(300)
                .build();


        PointMaster entity = pointSettingRequestDto.toEntity();
        PointMaster save = pointMasterRepository.save(entity);

    }
}
