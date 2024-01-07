package com.impacus.maketplace.service;

import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.user.response.UserDTO;
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
    void test2() {
        UserDTO userDTO = UserDTO.builder()
                .id(5L)
                .build();
       pointService.initPointMaster(userDTO);
    }

    @Test
    void test3() {
        boolean flag = pointMasterRepository.existsPointMasterByUserId(12L);
        System.out.println(flag);
    }

    @Test
    void test4() {
        PointRequestDto requestDto = PointRequestDto.builder()
                .pointCode("10") // SAVE
                .userId(5L)
                .savePoint(900)
                .build();
        pointService.changePoint(requestDto);
    }
}
