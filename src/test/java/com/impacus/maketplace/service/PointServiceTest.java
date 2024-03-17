package com.impacus.maketplace.service;

import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.repository.PointHistoryRepository;
import com.impacus.maketplace.repository.PointMasterRepository;
import com.impacus.maketplace.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

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
                .id(9L)
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
                .userId(22L)
                .savePoint(100000)
                .build();
        pointService.changePoint(requestDto);
    }

    @Test
    void test5() {
        PointHistorySearchDto pointHistorySearchDto = new PointHistorySearchDto();
        pointHistorySearchDto.setUserId(9L);
        List<PointHistoryDto> pointHistory = pointService.findPointHistory(pointHistorySearchDto);
        System.out.println(pointHistory);
        pointHistory.forEach(i -> System.out.println("\n" + i));
    }

//    @Test
//    void test6() {
//        LocalDateTime localDateTime = LocalDateTime.now().minusMonths(6);
//        long count = pointHistoryRepository.findByCreateAtGreaterThanEqualAndExpiredAtIsNotNull(localDateTime).stream().count();
//        System.out.println("count = " + count); // 10
//        pointHistoryRepository.findByCreateAtGreaterThanEqualAndExpiredAtIsNotNull(localDateTime).forEach(i -> System.out.println("create : " + i.getCreateAt() + " expire : " + i.getExpiredAt()));
//    }

    @Test
    void test7() {
        LocalDate localDate = LocalDate.now().minusMonths(5);
        System.out.println(localDate);
    }


}
