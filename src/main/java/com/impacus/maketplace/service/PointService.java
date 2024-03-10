package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.PointType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.ObjectCopyHelper;
import com.impacus.maketplace.dto.EmailDto;
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
    private final EmailService emailService;

    private final ObjectCopyHelper objectCopyHelper;
    private final Integer CELEBRATION_POINT = 5000;
    private final Integer FIRST_DORMANCY_POINT = 1500;
    private final Integer SECOND_DORMANCY_POINT = 2000;

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

    //TODO: 6개월 지난 시점에 상태  dormancy_update_at update
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDormancyUser() {
        LocalDate nextUpdateAt = LocalDateTime.now().plusMonths(1).toLocalDate();
        LocalDate nowDate = LocalDate.now();
        List<User> usersToUpdate = userRepository.findByUpdateDormancyAtAndFirstDormancyIsTrueOrSecondDormancyIsTrue(nowDate);
        for (User user : usersToUpdate) {
            if (user.getFirstDormancy() || user.getSecondDormancy()) { // 임시 휴면 회원 || 12개월 휴면 회원
                PointMaster pointMaster = pointMasterRepository.findByUserId(user.getId())
                        .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_POINT_MASTER));

                Integer currentAvailablePoint = pointMaster.getAvailablePoint();
                Integer currentUserScore = pointMaster.getUserScore();
                UserLevel currentUserLevel = pointMaster.getUserLevel();

                int underscoreIndex = user.getEmail().indexOf("_") + 1;
                String realUserEmail = user.getEmail().substring(underscoreIndex);

                switch (user.getDormancyMonths()) {
                    case 5, 6 -> {
                        user.setUpdateDormancyAt(nextUpdateAt);
                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
                    }
                    case 7, 8, 9 -> {
                        if (currentAvailablePoint <= FIRST_DORMANCY_POINT) {
                            pointMaster.setAvailablePoint(0);
                        } else {
                            pointMaster.setAvailablePoint(currentAvailablePoint - FIRST_DORMANCY_POINT);
                        }
                        if (currentUserScore <= FIRST_DORMANCY_POINT) {
                            pointMaster.setUserScore(0);
                        } else {
                            pointMaster.setUserScore(currentUserScore - FIRST_DORMANCY_POINT);
                        }

                        PointHistory pointHistory = PointHistory.builder()
                                .pointMasterId(pointMaster.getId())
                                .pointType(PointType.DORMANCY)
                                .changePoint(FIRST_DORMANCY_POINT)
                                .isManual(false)
                                .build();
                        pointHistoryRepository.save(pointHistory);

                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
                        if (!StringUtils.equals(changeUserLevel,currentUserLevel)) {
                            pointMaster.setUserLevel(changeUserLevel);
                        }

                        user.setUpdateDormancyAt(nextUpdateAt);
                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
                    }
                    case 10 -> {

                        if (currentAvailablePoint <= SECOND_DORMANCY_POINT) {
                            pointMaster.setAvailablePoint(0);
                        } else {
                            pointMaster.setAvailablePoint(currentAvailablePoint - SECOND_DORMANCY_POINT);
                        }
                        if (currentUserScore <= SECOND_DORMANCY_POINT) {
                            pointMaster.setUserScore(0);
                        } else {
                            pointMaster.setUserScore(currentUserScore - SECOND_DORMANCY_POINT);
                        }

                        PointHistory pointHistory = PointHistory.builder()
                                .pointMasterId(pointMaster.getId())
                                .pointType(PointType.DORMANCY)
                                .changePoint(SECOND_DORMANCY_POINT)
                                .isManual(false)
                                .build();
                        pointHistoryRepository.save(pointHistory);

                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
                        if (!StringUtils.equals(changeUserLevel,currentUserLevel)) {
                            pointMaster.setUserLevel(changeUserLevel);
                        }

                        user.setUpdateDormancyAt(nextUpdateAt);
                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
                    }
                    case 11, 12 -> {
                        if (user.getDormancyMonths() == 11) {
                            //TODO: 11개월 휴먼 안내 메일
                            EmailDto emailDto = EmailDto.builder()
                                    .subject(MailType.DORMANCY_INFO.getSubject())
                                    .receiveEmail(realUserEmail)
                                    .build();
                            emailService.sendMail(emailDto, MailType.DORMANCY_INFO);
                        } else if (user.getDormancyMonths() == 12) {
                            //TODO: 휴면 계정으로 전환 및 dormancy_user 테이블에 이관
                            user.setSecondDormancy(true);

                            DormantUser dormantUser = objectCopyHelper.copyObject(user, DormantUser.class);
                            dormantUserRepository.save(dormantUser);
                        }

                        if (currentAvailablePoint <= SECOND_DORMANCY_POINT) {
                            pointMaster.setAvailablePoint(0);
                        } else {
                            pointMaster.setAvailablePoint(currentAvailablePoint - SECOND_DORMANCY_POINT);
                        }
                        if (currentUserScore <= SECOND_DORMANCY_POINT) {
                            pointMaster.setUserScore(0);
                        } else {
                            pointMaster.setUserScore(currentUserScore - SECOND_DORMANCY_POINT);
                        }

                        PointHistory pointHistory = PointHistory.builder()
                                .pointMasterId(pointMaster.getId())
                                .pointType(PointType.DORMANCY)
                                .changePoint(SECOND_DORMANCY_POINT)
                                .isManual(false)
                                .build();
                        pointHistoryRepository.save(pointHistory);

                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
                        if (!StringUtils.equals(changeUserLevel,currentUserLevel)) {
                            pointMaster.setUserLevel(changeUserLevel);
                        }

                        user.setUpdateDormancyAt(nextUpdateAt);
                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
                    }
                    case 13, 14 -> {
                        if (user.getDormancyMonths() == 14) {
                            //TODO: 데이터 삭제 관련 안내 메일 전송
                            EmailDto emailDto = EmailDto.builder()
                                    .subject(MailType.USER_DELETE.getSubject())
                                    .receiveEmail(realUserEmail)
                                    .build();
                            emailService.sendMail(emailDto, MailType.USER_DELETE);

                        }

                        if (currentAvailablePoint <= SECOND_DORMANCY_POINT) {
                            pointMaster.setAvailablePoint(0);
                        } else {
                            pointMaster.setAvailablePoint(currentAvailablePoint - SECOND_DORMANCY_POINT);
                        }
                        if (currentUserScore <= SECOND_DORMANCY_POINT) {
                            pointMaster.setUserScore(0);
                        } else {
                            pointMaster.setUserScore(currentUserScore - SECOND_DORMANCY_POINT);
                        }

                        PointHistory pointHistory = PointHistory.builder()
                                .pointMasterId(pointMaster.getId())
                                .pointType(PointType.DORMANCY)
                                .changePoint(SECOND_DORMANCY_POINT)
                                .isManual(false)
                                .build();
                        pointHistoryRepository.save(pointHistory);

                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
                        if (!StringUtils.equals(changeUserLevel,currentUserLevel)) {
                            pointMaster.setUserLevel(changeUserLevel);
                        }

                        user.setUpdateDormancyAt(nextUpdateAt);
                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
                    }
                    case 16 -> {
                        //TODO: 데이터 자동 삭제 ( 메시지, 문의, 리뷰 관련 모두 삭제)
                    }

                }
            }
        }
    }

}
