package com.impacus.maketplace.service.point.greenLabelPoint;

import com.impacus.maketplace.common.enumType.error.PointErrorType;
import com.impacus.maketplace.common.enumType.point.PointStatus;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.PointUsageStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointAllocation;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistory;
import com.impacus.maketplace.entity.point.greenLablePoint.GreenLabelPointHistoryRelation;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointAllocationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRelationRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointHistoryRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.GreenLabelPointRepository;
import com.impacus.maketplace.repository.point.greenLabelPoint.mapping.NotUsedGreenLabelPointAllocationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("[비즈니스 로직] - 그린 라벨 포인트 지급")
class GreenLabelPointAllocationServiceTest {
    @InjectMocks
    private GreenLabelPointAllocationService allocationService;
    @Mock
    private GreenLabelPointRepository greenLabelPointRepository;
    @Mock
    private GreenLabelPointAllocationRepository allocationRepository;
    @Mock
    private GreenLabelPointHistoryRepository historyRepository;
    @Mock
    private GreenLabelPointHistoryRelationRepository relationRepository;

    @Test
    @DisplayName("[정상 케이스] 그린 라벨 포인트 지급한다.")
    void testPayGreenLabelPoint_success() {
        // give
        Long userId = 1L;
        PointType pointType = PointType.PURCHASE_GENERAL_PRODUCT;
        Long tradePoint = 100L;

        // when
        when(greenLabelPointRepository.findGreenLabelPointByUserId(userId)).thenReturn(200L);
        when(allocationRepository.save(any(GreenLabelPointAllocation.class))).thenReturn(null);
        when(historyRepository.save(any(GreenLabelPointHistory.class))).thenReturn(null);
        when(relationRepository.save(any(GreenLabelPointHistoryRelation.class))).thenReturn(null);
        boolean result = allocationService.payGreenLabelPoint(userId, pointType, tradePoint);

        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("[오류 케이스 - INVALID_POINT] 포인트를 지급하지 못 한다.")
    void testPayGreenLabelPoint_invalidPoint() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.CHECK;
        Long tradePoint = -100L;

        // when
        CustomException exception = assertThrows(CustomException.class, () ->
                allocationService.payGreenLabelPoint(userId, pointType, tradePoint));

        // then
        assertThat(exception.getErrorType()).isEqualTo(PointErrorType.INVALID_POINT);
    }

    @Test
    @DisplayName("[정상 케이스] 출석 포인트는 하루에 한 번만 지급할 수 있다.")
    void testCheckIsGreenPointsAwardable_check() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.CHECK;
        LocalDateTime allocatedPointAt = LocalDate.now().atStartOfDay().plusHours(2);
        Long tradePoint = PointType.CHECK.getAllocatedPoints();

        // when
        when(allocationRepository.findRecentAllocatedPointAtByUserIdAndPointType(userId, pointType)).thenReturn(allocatedPointAt);
        boolean result = allocationService.payGreenLabelPoint(userId, pointType, tradePoint);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("[정상 케이스] 상품 공유는 하루에 두 번만 지급할 수 있다.")
    void testCheckIsGreenPointsAwardable_shareProduct() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.SHARE_PRODUCT;
        Long allocatedPointCnt = 2L;
        Long tradePoint = PointType.SHARE_PRODUCT.getAllocatedPoints();

        // when
        when(allocationRepository.findAllocatedPointCntByUserIdAndPointType(userId, pointType)).thenReturn(allocatedPointCnt);
        boolean result = allocationService.payGreenLabelPoint(userId, pointType, tradePoint);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("[정상 케이스] SNS태그는 격주에 한 번만 지급할 수 있다.")
    void testCheckIsGreenPointsAwardable_snsTag() {
        // given
        Long userId = 1L;
        PointType pointType = PointType.SNS_TAG;
        LocalDateTime allocatedPointAt = LocalDateTime.now().minusWeeks(1);
        Long tradePoint = PointType.SNS_TAG.getAllocatedPoints();

        // when
        when(allocationRepository.findRecentAllocatedPointAtByUserIdAndPointType(userId, pointType)).thenReturn(allocatedPointAt);
        boolean result = allocationService.payGreenLabelPoint(userId, pointType, tradePoint);

        // then
        assertFalse(result);
    }

    @Test
    @DisplayName("[정상 케이스] 그린 라벨 포인트를 차감한다.")
    void testDeductPoints_success() {
        // given
        Long userId = 1L;
        PointType type = PointType.PURCHASE_GENERAL_PRODUCT;
        PointUsageStatus pointStatus = PointUsageStatus.UNUSED;
        Long usedPoints = 100L;
        boolean allowsNegativeBalance = false;

        NotUsedGreenLabelPointAllocationDTO allocation1 = new NotUsedGreenLabelPointAllocationDTO(1L, 50L, LocalDateTime.now().minusMinutes(1), pointStatus);
        NotUsedGreenLabelPointAllocationDTO allocation2 = new NotUsedGreenLabelPointAllocationDTO(2L, 70L, LocalDateTime.now(), pointStatus);
        List<NotUsedGreenLabelPointAllocationDTO> allocations = Arrays.asList(allocation1, allocation2);

        GreenLabelPointHistory history = GreenLabelPointHistory.of(
                userId,
                type,
                PointStatus.USE,
                usedPoints * (-1),
                0L
        );

        // when
        when(greenLabelPointRepository.findGreenLabelPointByUserId(userId)).thenReturn(200L);
        when(allocationRepository.findNotUsedGreenLabelPointByUserId(userId)).thenReturn(allocations);
        when(historyRepository.save(any(GreenLabelPointHistory.class)))
                .thenReturn(history);
        when(relationRepository.save(any(GreenLabelPointHistoryRelation.class))).thenReturn(null);
        Long historyId = allocationService.deductPoints(userId, type, usedPoints, allowsNegativeBalance);

        // then
        verify(greenLabelPointRepository).updateGreenLabelPointByUserId(userId, 100L);
        verify(historyRepository).save(any(GreenLabelPointHistory.class));
        verify(relationRepository, times(2)).save(any(GreenLabelPointHistoryRelation.class));
        verify(allocationRepository, times(2)).updateGreenLabelPointAllocationById(anyLong(), any(PointUsageStatus.class), anyLong());
    }

    @Test
    @DisplayName("[오류 케이스 - INVALID_POINT] 포인트를 차감하지 못 한다.")
    void testDeductPoints_insufficientBalance() {
        //given
        Long userId = 1L;
        PointType type = PointType.PURCHASE_GENERAL_PRODUCT;
        Long usedPoints = 300L;
        boolean allowsNegativeBalance = false;

        // when
        when(greenLabelPointRepository.findGreenLabelPointByUserId(userId)).thenReturn(200L);
        CustomException exception = assertThrows(CustomException.class, () ->
                allocationService.deductPoints(userId, type, usedPoints, allowsNegativeBalance));

        // then
        assertThat(exception.getErrorType()).isEqualTo(PointErrorType.INVALID_POINT);
    }

}