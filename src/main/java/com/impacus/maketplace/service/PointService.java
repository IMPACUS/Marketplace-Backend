package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.CurrentPointInfoDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.point.response.PointInfoDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.point.PointHistory;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.entity.user.DormantUser;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.DormantUserRepository;
import com.impacus.maketplace.repository.PointHistoryRepository;
import com.impacus.maketplace.repository.PointMasterRepository;
import com.impacus.maketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import security.CustomUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PointService {

    private final PointMasterRepository pointMasterRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserRepository userRepository;
    private final DormantUserRepository dormantUserRepository;

    private final ObjectCopyHelper objectCopyHelper;
    private final Integer CELEBRATION_POINT = 5000;
    private final Integer DORMANCY_POINT = 150;

    @Transactional
    public boolean initPointMaster(UserDTO userDTO) {
        if (pointMasterRepository.existsPointMasterByUserId(userDTO.id())) {
            return true;
        }

        PointMaster pointMaster = PointMaster.builder()
                .userId(userDTO.id())
                .availablePoint(CELEBRATION_POINT)
                .userScore(CELEBRATION_POINT)
                .isBronze(true)
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

        changeUpLevel(pointMaster, userScore, currentUserLevel);

        PointMasterDto pointMasterDto = new PointMasterDto(pointMaster);
        return pointMasterDto;
    }

    //1월16일 12시 10분
    private static void changeUpLevel(PointMaster pointMaster, Integer userScore, UserLevel currentUserLevel) {
        UserLevel changeUserLevel = UserLevel.fromScore(userScore);
        if (!StringUtils.equals(changeUserLevel, currentUserLevel)) {
            pointMaster.setUserLevel(changeUserLevel);

            Integer currentAvailablePoint = pointMaster.getAvailablePoint();
            switch (changeUserLevel) {
                case BRONZE  -> pointMaster.setBronze(true);
                case ROOKIE  -> {
                    if (!pointMaster.isRookie()) {
                        pointMaster.setAvailablePoint(currentAvailablePoint + UserLevel.ROOKIE.getCelebrationPoint());
                        pointMaster.setUserScore(currentAvailablePoint + UserLevel.ROOKIE.getCelebrationPoint());
                    } else {
                        pointMaster.setAvailablePoint(currentAvailablePoint + (UserLevel.ROOKIE.getCelebrationPoint() / 2));
                        pointMaster.setUserScore(currentAvailablePoint + (UserLevel.ROOKIE.getCelebrationPoint() / 2));
                    }
                    pointMaster.setRookie(true);
                }
                case SILVER  -> {
                    if (!pointMaster.isSilver()) {
                        pointMaster.setAvailablePoint(currentAvailablePoint + UserLevel.SILVER.getCelebrationPoint());
                        pointMaster.setUserScore(currentAvailablePoint + UserLevel.SILVER.getCelebrationPoint());
                    } else {
                        pointMaster.setAvailablePoint(currentAvailablePoint + (UserLevel.SILVER.getCelebrationPoint() / 2));
                        pointMaster.setUserScore(currentAvailablePoint + (UserLevel.SILVER.getCelebrationPoint() / 2));
                    }
                    pointMaster.setSilver(true);
                }
                case GOLD    -> {
                    if (!pointMaster.isGold()) {
                        pointMaster.setAvailablePoint(currentAvailablePoint + UserLevel.GOLD.getCelebrationPoint());
                        pointMaster.setUserScore(currentAvailablePoint + UserLevel.GOLD.getCelebrationPoint());
                    } else {
                        pointMaster.setAvailablePoint(currentAvailablePoint + (UserLevel.GOLD.getCelebrationPoint() / 2));
                        pointMaster.setUserScore(currentAvailablePoint + (UserLevel.GOLD.getCelebrationPoint() / 2));
                    }
                    pointMaster.setGold(true);
                }
                case ECO_VIP -> pointMaster.setEcoVip(true);
            }
        }
    }

    public List<PointHistoryDto> findPointHistory(PointHistorySearchDto pointHistorySearchDto) {
        return pointHistoryRepository.findAllPointHistory(pointHistorySearchDto);
    }

    public PointInfoDto findMyPointInfo(CustomUserDetails user) {
        PointMaster pointMaster = pointMasterRepository.findByUserId(user.getId()).orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER));
        PointInfoDto pointInfoDto = new PointInfoDto(pointMaster.getUserScore());
        return pointInfoDto;
    }

    public CurrentPointInfoDto findCurrentMyPointStatus(CustomUserDetails user) {

        CurrentPointInfoDto data = pointMasterRepository.findByUserIdForMyCurrentPointStatus(user.getId());
        if (data == null) {
            throw new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER);
        }
        return data;
    }

    /**
     * 6개월의 유효기간이 있는 포인트 내역은 기간이 지나면 소멸 스케줄러  -> 추후 배치로 변경 예정
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateDisappearPoint() {
        LocalDateTime sixMonthAgoDate = LocalDateTime.now().minusMonths(6).minusDays(1);
        List<PointHistory> sixMonthDataList = pointHistoryRepository.findByCreateAtGreaterThanEqualAndExpiredAtIsNotNullAndExpiredCheckIsFalse(sixMonthAgoDate);
        for (PointHistory pointHistory : sixMonthDataList) {
            LocalDate expiredDate = pointHistory.getExpiredAt().plusDays(1).toLocalDate();
            LocalDate currentDate = LocalDate.now();

            if (expiredDate.isEqual(currentDate) || expiredDate.isBefore(currentDate)) {
                pointHistory.setExpiredCheck(true);
                Long pointMasterId = pointHistory.getPointMasterId();

                PointMaster pointMaster = pointMasterRepository.findById(pointMasterId).orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER));
                int saveScore = pointMaster.getAvailablePoint() - pointHistory.getChangePoint();
                pointMaster.setAvailablePoint(saveScore);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void addDormancyUser() {
        /**
         * 회원 휴면 전환 기간 1년 (일시적 중지)
         * - 1차(휴면)
         *  : 12개월간 로그인을 안했을 경우 30일 전에 회원에게 휴면 안내 메일, 문자(알림) 전송
         *  : 12개월 채울시, 휴면 계정으로 자동 전환된다.
         *  : 휴면 상태에서  로그인 시 자동 휴면 해제
         *
         * - 2차(초기화)
         *  : 휴면 전환 후 120일(4개월)이상 해제 하지 않은 경우: 계정 및 모든 데이터 삭제( 복구불가 )
         *
         */
        LocalDateTime endDate = LocalDate.now().atStartOfDay();
        LocalDateTime startDate = endDate.minusMonths(6);
        List<Long> findDormancyUser = pointHistoryRepository.findAllNoUseUser(startDate, endDate);

        for (Long userId : findDormancyUser) {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_EMAIL));
            user.setIsDormancy(true);
            user.setDormancyDateTime(LocalDate.now().atStartOfDay());

            DormantUser dormantUser = objectCopyHelper.copyObject(user, DormantUser.class);
            dormantUser.setDormancyUpdateDateTime(LocalDate.now().plusWeeks(2).atStartOfDay());

            dormantUserRepository.save(dormantUser);
            userRepository.deleteById(userId);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void reductionPointForDormantUsers() {
        /**
         * 회원 휴면 -> 가용/레벨 포인트 관련 유효기간
         *   : 6개월간 로그인 하지 않았을 경우, 30일 전에 회원에게 포인소멸 안내 매일, 문자(알림) 전송
         *   : 6개월 채울 시, 첫 3개월 간 1달 간격으로 1500P 감소, 이후 한달 간격으로 2000P 감소
         *
         * 이 때, 2차라면 모든 포인트, 쿠폰 관련 데이터 삭제, 그 전까지는 유지 및 소멸
         */

        /**
         * 그럼 마지막 로그인 시각이 현재보다 5개월 후일 경우, 회원에게 포인트 소멸 안내 메일 및 문자 전송
         * 이 때 1달이 지난 6개월 시점에, 7,8,9 개월 지난 시점에 1500P 소멸
         * 10개월이 지난 시점으로는 2000P 소멸
         * 이 때 11개월이 지나면 회원에게 휴면 안내 메일, 문자(알림) 발송
         * 12개월 채울시 휴면 계정으로 (상태값) 자동 전환 ,
         * 14개월 시점에 데이터 삭제 관련 안내 메일 전송
         * (16개월 전까지 로그인 한다면, 자동 휴면 해제)
         * 하지만 마지막 로그인 시점 이후 16개월 이후에는 관련 데이터 삭제 (이 때 메세지, 문의, 리뷰 관련은 모두 삭제)
         *
         * 따라서 16개월 초기화 전까지는 매달 포인트 차감!
         *
         *
         */
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<DormantUser> dormantUsers = dormantUserRepository.findByDormancyUpdateDateTime(today);
        for (DormantUser dormantUser : dormantUsers) {
            PointMaster pointMaster = pointMasterRepository.findByUserId(dormantUser.getId()).orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER));

            Integer currentAvailablePoint = pointMaster.getAvailablePoint();
            Integer currentUserScore = pointMaster.getUserScore();

            if (currentAvailablePoint < DORMANCY_POINT) {
                pointMaster.setAvailablePoint(0);
            } else {
                pointMaster.setAvailablePoint(currentAvailablePoint - DORMANCY_POINT);
            }
            pointMaster.setUserScore(currentUserScore- DORMANCY_POINT);

            PointHistory pointHistory = PointHistory.builder()
                    .pointMasterId(pointMaster.getId())
                    .pointType(PointType.DORMANCY)
                    .changePoint(DORMANCY_POINT)
                    .isManual(true)
                    .build();

            pointHistoryRepository.save(pointHistory);

            UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
            if (!StringUtils.equals(changeUserLevel, pointMaster.getUserLevel())) {
                pointMaster.setUserLevel(changeUserLevel);
            }

            dormantUser.setDormancyUpdateDateTime(today.plusWeeks(2));
        }
    }
}
