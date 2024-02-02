package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
import com.impacus.maketplace.dto.point.request.PointRequestDto;
import com.impacus.maketplace.dto.point.response.CurrentPointInfoDto;
import com.impacus.maketplace.dto.point.response.PointHistoryDto;
import com.impacus.maketplace.dto.point.response.PointInfoDto;
import com.impacus.maketplace.dto.point.response.PointMasterDto;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.point.PointHistory;
import com.impacus.maketplace.entity.point.PointMaster;
import com.impacus.maketplace.entity.user.DormancyUser;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.DormancyUserRepository;
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
    private final DormancyUserRepository dormancyUserRepository;
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
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(6);
        List<Long> findDormancyUser = pointHistoryRepository.findAllWithNoUseOrSavePoint(startDate, endDate);

        for (Long userId : findDormancyUser) {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_EMAIL));
            user.setIsDormancy(true);
            user.setDormancyDateTime(LocalDateTime.now());
            
            DormancyUser dormancyUser = DormancyUser.builder()
                    .userId(userId)
                    .userName(user.getName())
                    .updateDormancyDateTime(LocalDateTime.now().plusDays(14))
                    .build();

            dormancyUserRepository.save(dormancyUser);
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void deductionPointForDormancyUser() {
        /**
         * 포인트 소멸은 6개월간 구매 혹은 포인트 적립이 없을시 2주마다 -150P씩 소멸하는 것이고,
         * 예를 들어 현재 99,000P를 소유하고 있는 BRONZE 에서
         * 상품 구매로 인한 +1,200P를 받아 100,200 가 되었을 때, 30,000포인트를 지급합니다.
         * 따라서 130,200이 되어 있을 거고,
         * 여기서 장기간 미사용 시 , 포인트 소멸이 일어나 Rookie 의 포인트가 내려갈 경우 Bronze 레벨로 변할거고
         * 다시 포인트를 적립하여 Rookie로 올경우 그에 절반인 15,000포인트만 적립이 되는 것
         */
        List<DormancyUser> allDormancyUserList = dormancyUserRepository.findAll();

    }

}
