//package com.impacus.maketplace.service;
//
//import com.impacus.maketplace.common.enumType.PointType;
//import com.impacus.maketplace.common.enumType.error.CommonErrorType;
//import com.impacus.maketplace.common.enumType.point.PointManageType;
//import com.impacus.maketplace.common.enumType.user.UserLevel;
//import com.impacus.maketplace.common.exception.CustomException;
//import com.impacus.maketplace.common.utils.ObjectCopyHelper;
//import com.impacus.maketplace.dto.point.request.PointHistorySearchDto;
//import com.impacus.maketplace.dto.point.request.PointManageDTO;
//import com.impacus.maketplace.dto.point.request.PointRequestDTO;
//import com.impacus.maketplace.dto.point.response.CurrentPointInfoDTO;
//import com.impacus.maketplace.dto.point.response.PointHistoryDTO;
//import com.impacus.maketplace.dto.point.response.PointInfoDto;
//import com.impacus.maketplace.dto.point.response.PointMasterDTO;
//import com.impacus.maketplace.dto.user.response.UserDTO;
//import com.impacus.maketplace.entity.point.PointHistory;
//import com.impacus.maketplace.entity.point.PointMaster;
//import com.impacus.maketplace.entity.user.User;
//import com.impacus.maketplace.repository.DormantUserRepository;
//import com.impacus.maketplace.repository.PointHistoryRepository;
//import com.impacus.maketplace.repository.PointMasterRepository;
//import com.impacus.maketplace.repository.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.thymeleaf.util.StringUtils;
//import security.CustomUserDetails;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional(readOnly = true)
//@Slf4j
//public class PointService {
//
//    private final PointMasterRepository pointMasterRepository;
//    private final PointHistoryRepository pointHistoryRepository;
//    private final UserRepository userRepository;
//    private final DormantUserRepository dormantUserRepository;
//    private final EmailService emailService;
//
//    private final ObjectCopyHelper objectCopyHelper;
//    private final Integer CELEBRATION_POINT = 5000;
//    private final Integer FIRST_DORMANCY_POINT = 1500;
//    private final Integer SECOND_DORMANCY_POINT = 2000;
//
//
//    @Transactional
//    public PointMasterDTO changePoint(PointRequestDTO pointRequestDto) {
//        PointMaster pointMaster = pointMasterRepository.findByUserIdForUpdate(pointRequestDto.getUserId()).orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_POINT_MASTER));
//        LocalDateTime settingExpiredAt = null;
//        if (pointRequestDto.getPointTypeEnum() != PointType.USE &&
//                pointRequestDto.getPointTypeEnum() != PointType.EXPIRE) {
//            settingExpiredAt = LocalDateTime.now().plusMonths(6L);
//        }
//        PointHistory pointHistory = PointHistory.builder()
//                .changePoint(pointRequestDto.getSavePoint())
//                .pointMasterId(pointMaster.getId())
//                .pointType(pointRequestDto.getPointTypeEnum())
//                .expiredAt(settingExpiredAt)
//                .build();
//        pointHistoryRepository.save(pointHistory);
//
//        Integer availablePoint = pointMaster.getAvailablePoint();
//        Integer userScore = pointMaster.getUserScore();
//        Integer savePoint = pointRequestDto.getSavePoint();
//
//        UserLevel currentUserLevel = pointMaster.getUserLevel();
//
//        switch (pointHistory.getPointType()) {
//            case USE -> {
//                pointMaster.setAvailablePoint(availablePoint - savePoint);
//            }
//            case SAVE -> {
//                pointMaster.setAvailablePoint(availablePoint + savePoint);
//                pointMaster.setUserScore(userScore + savePoint);
//                userScore += savePoint;
//
//            }
//        }
//
//        changeUpLevel(pointMaster, userScore, currentUserLevel);
//
//        PointMasterDTO pointMasterDto = new PointMasterDTO(pointMaster);
//        return pointMasterDto;
//    }
//
//    public List<PointHistoryDTO> findPointHistory(PointHistorySearchDto pointHistorySearchDto) {
//        return pointHistoryRepository.findAllPointHistory(pointHistorySearchDto);
//    }
//
//    public PointInfoDto findMyPointInfo(CustomUserDetails user) {
//        PointMaster pointMaster = pointMasterRepository.findByUserIdForUpdate(user.getId()).orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_POINT_MASTER));
//        PointInfoDto pointInfoDto = new PointInfoDto(pointMaster.getUserScore());
//        return pointInfoDto;
//    }
//
//    public CurrentPointInfoDTO findCurrentMyPointStatus(CustomUserDetails user) {
//
//        CurrentPointInfoDTO data = pointMasterRepository.findByUserIdForMyCurrentPointStatus(user.getId());
//        if (data == null) {
//            throw new CustomException(CommonErrorType.NOT_EXISTED_POINT_MASTER);
//        }
//        return data;
//    }
//
//    /**
//     * 6개월의 유효기간이 있는 포인트 내역은 기간이 지나면 소멸 스케줄러  -> 추후 배치로 변경 예정
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Transactional
//    public void updateDisappearPoint() {
//        LocalDateTime sixMonthAgoDate = LocalDateTime.now().minusMonths(6).minusDays(1);
//        List<PointHistory> sixMonthDataList = pointHistoryRepository.findByCreateAtGreaterThanEqualAndExpiredAtIsNotNullAndExpiredCheckIsFalse(sixMonthAgoDate);
//        for (PointHistory pointHistory : sixMonthDataList) {
//            LocalDate expiredDate = pointHistory.getExpiredAt().plusDays(1).toLocalDate();
//            LocalDate currentDate = LocalDate.now();
//
//            if (expiredDate.isEqual(currentDate) || expiredDate.isBefore(currentDate)) {
//                pointHistory.setExpiredCheck(true);
//                Long pointMasterId = pointHistory.getPointMasterId();
//
//                PointMaster pointMaster = pointMasterRepository.findById(pointMasterId).orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_POINT_MASTER));
//                int saveScore = pointMaster.getAvailablePoint() - pointHistory.getChangePoint();
//                pointMaster.setAvailablePoint(saveScore);
//            }
//        }
//    }
//
//    //TODO: 6개월 지난 시점에 상태  dormancy_update_at update
//    @Transactional
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void updateDormancyUser() {
//        // TODO user entity 정리하면서 관련 column 데이터 삭제
////        LocalDate nextUpdateAt = LocalDateTime.now().plusMonths(1).toLocalDate();
////        LocalDate nowDate = LocalDate.now();
////        List<User> usersToUpdate = userRepository.findByUpdateDormancyAtAndFirstDormancyIsTrueOrSecondDormancyIsTrue(nowDate);
////        for (User user : usersToUpdate) {
////            if (user.getFirstDormancy() || user.getSecondDormancy()) { // 임시 휴면 회원 || 12개월 휴면 회원
////                PointMaster pointMaster = pointMasterRepository.findByUserIdForUpdate(user.getId())
////                        .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_POINT_MASTER));
////
////                Integer currentAvailablePoint = pointMaster.getAvailablePoint();
////                Integer currentUserScore = pointMaster.getUserScore();
////                UserLevel currentUserLevel = pointMaster.getUserLevel();
////
////                int underscoreIndex = user.getEmail().indexOf("_") + 1;
////                String realUserEmail = user.getEmail().substring(underscoreIndex);
////
////                switch (user.getDormancyMonths()) {
////                    case 5, 6 -> {
////                        user.setUpdateDormancyAt(nextUpdateAt);
////                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
////                    }
////                    case 7, 8, 9 -> {
////                        if (currentAvailablePoint <= FIRST_DORMANCY_POINT) {
////                            pointMaster.setAvailablePoint(0);
////                        } else {
////                            pointMaster.setAvailablePoint(currentAvailablePoint - FIRST_DORMANCY_POINT);
////                        }
////                        if (currentUserScore <= FIRST_DORMANCY_POINT) {
////                            pointMaster.setUserScore(0);
////                        } else {
////                            pointMaster.setUserScore(currentUserScore - FIRST_DORMANCY_POINT);
////                        }
////
////                        PointHistory pointHistory = PointHistory.builder()
////                                .pointMasterId(pointMaster.getId())
////                                .pointType(PointType.DORMANCY)
////                                .changePoint(FIRST_DORMANCY_POINT)
////                                .isManual(false)
////                                .build();
////                        pointHistoryRepository.save(pointHistory);
////
////                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
////                        if (!StringUtils.equals(changeUserLevel, currentUserLevel)) {
////                            pointMaster.setUserLevel(changeUserLevel);
////                        }
////
////                        user.setUpdateDormancyAt(nextUpdateAt);
////                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
////                    }
////                    case 10 -> {
////
////                        if (currentAvailablePoint <= SECOND_DORMANCY_POINT) {
////                            pointMaster.setAvailablePoint(0);
////                        } else {
////                            pointMaster.setAvailablePoint(currentAvailablePoint - SECOND_DORMANCY_POINT);
////                        }
////                        if (currentUserScore <= SECOND_DORMANCY_POINT) {
////                            pointMaster.setUserScore(0);
////                        } else {
////                            pointMaster.setUserScore(currentUserScore - SECOND_DORMANCY_POINT);
////                        }
////
////                        PointHistory pointHistory = PointHistory.builder()
////                                .pointMasterId(pointMaster.getId())
////                                .pointType(PointType.DORMANCY)
////                                .changePoint(SECOND_DORMANCY_POINT)
////                                .isManual(false)
////                                .build();
////                        pointHistoryRepository.save(pointHistory);
////
////                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
////                        if (!StringUtils.equals(changeUserLevel, currentUserLevel)) {
////                            pointMaster.setUserLevel(changeUserLevel);
////                        }
////
////                        user.setUpdateDormancyAt(nextUpdateAt);
////                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
////                    }
////                    case 11, 12 -> {
////                        if (user.getDormancyMonths() == 11) {
////                            //TODO: 11개월 휴먼 안내 메일
////                            EmailDto emailDto = EmailDto.builder()
////                                    .subject(MailType.DORMANCY_INFO.getSubject())
////                                    .receiveEmail(realUserEmail)
////                                    .build();
////                            emailService.sendMail(emailDto, MailType.DORMANCY_INFO);
////                        } else if (user.getDormancyMonths() == 12) {
////                            //TODO: 휴면 계정으로 전환 및 dormancy_user 테이블에 이관
////                            user.setSecondDormancy(true);
////
////                            DormantUser dormantUser = objectCopyHelper.copyObject(user, DormantUser.class);
////                            dormantUserRepository.save(dormantUser);
////                        }
////
////                        if (currentAvailablePoint <= SECOND_DORMANCY_POINT) {
////                            pointMaster.setAvailablePoint(0);
////                        } else {
////                            pointMaster.setAvailablePoint(currentAvailablePoint - SECOND_DORMANCY_POINT);
////                        }
////                        if (currentUserScore <= SECOND_DORMANCY_POINT) {
////                            pointMaster.setUserScore(0);
////                        } else {
////                            pointMaster.setUserScore(currentUserScore - SECOND_DORMANCY_POINT);
////                        }
////
////                        PointHistory pointHistory = PointHistory.builder()
////                                .pointMasterId(pointMaster.getId())
////                                .pointType(PointType.DORMANCY)
////                                .changePoint(SECOND_DORMANCY_POINT)
////                                .isManual(false)
////                                .build();
////                        pointHistoryRepository.save(pointHistory);
////
////                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
////                        if (!StringUtils.equals(changeUserLevel, currentUserLevel)) {
////                            pointMaster.setUserLevel(changeUserLevel);
////                        }
////
////                        user.setUpdateDormancyAt(nextUpdateAt);
////                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
////                    }
////                    case 13, 14 -> {
////                        if (user.getDormancyMonths() == 14) {
////                            //TODO: 데이터 삭제 관련 안내 메일 전송
////                            EmailDto emailDto = EmailDto.builder()
////                                    .subject(MailType.USER_DELETE.getSubject())
////                                    .receiveEmail(realUserEmail)
////                                    .build();
////                            emailService.sendMail(emailDto, MailType.USER_DELETE);
////
////                        }
////
////                        if (currentAvailablePoint <= SECOND_DORMANCY_POINT) {
////                            pointMaster.setAvailablePoint(0);
////                        } else {
////                            pointMaster.setAvailablePoint(currentAvailablePoint - SECOND_DORMANCY_POINT);
////                        }
////                        if (currentUserScore <= SECOND_DORMANCY_POINT) {
////                            pointMaster.setUserScore(0);
////                        } else {
////                            pointMaster.setUserScore(currentUserScore - SECOND_DORMANCY_POINT);
////                        }
////
////                        PointHistory pointHistory = PointHistory.builder()
////                                .pointMasterId(pointMaster.getId())
////                                .pointType(PointType.DORMANCY)
////                                .changePoint(SECOND_DORMANCY_POINT)
////                                .isManual(false)
////                                .build();
////                        pointHistoryRepository.save(pointHistory);
////
////                        UserLevel changeUserLevel = UserLevel.fromScore(pointMaster.getUserScore());
////                        if (!StringUtils.equals(changeUserLevel, currentUserLevel)) {
////                            pointMaster.setUserLevel(changeUserLevel);
////                        }
////
////                        user.setUpdateDormancyAt(nextUpdateAt);
////                        user.setDormancyMonths(user.getDormancyMonths() + 1); // 현재 휴면 개월수 + 1
////                    }
////                    case 16 -> {
////                        //TODO: 데이터 자동 삭제 ( 메시지, 문의, 리뷰 관련 모두 삭제)
////                    }
////
////                }
////            }
////        }
//    }
//
//
//    /**
//     * ADMIN MANAGE POINT
//     * 1. 포인트 지급 / 수취
//     * 2. 포인트 이력 저장
//     */
//    @Transactional
//    public boolean pointManage(PointManageDTO pointManageDto) {
//        try {
//            PointMaster pointMaster = pointMasterRepository.findByUserIdForUpdate(pointManageDto.getUserId())
//                    .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_POINT_MASTER));
//
//            Integer currentAvailablePoint = pointMaster.getAvailablePoint();
//            Integer currentUserScore = pointMaster.getUserScore();
//            int manageAvailablePoint = pointManageDto.getManageAvailablePoint();
//            int manageUserScore = pointManageDto.getManageLevelPoint();
//            UserLevel currentUserLevel = pointMaster.getUserLevel();
//
//            if (pointManageDto.getPointManageType().equals(PointManageType.PROVIDE.getCode())) {   //    지급
//                if (manageAvailablePoint > 0) {
//                    pointMaster.setAvailablePoint(currentAvailablePoint + manageAvailablePoint);
//                }
//                if (manageUserScore > 0) {
//                    int resultUserScore = currentUserScore + manageUserScore;
//                    pointMaster.setUserScore(currentUserScore + manageUserScore);
//                    changeUpLevel(pointMaster, resultUserScore, currentUserLevel);
//                }
//            } else if (pointManageDto.getPointManageType().equals(PointManageType.RECEIVE.getCode())) {    //  수취
//                if (manageAvailablePoint > 0) {
//                    int resultAvailablePoint = currentAvailablePoint - manageAvailablePoint;
//                    if (resultAvailablePoint <= 0) {
//                        throw new CustomException(CommonErrorType.INVALID_POINT_MANAGE);
//                    } else {
//                        pointMaster.setAvailablePoint(resultAvailablePoint);
//                    }
//                }
//                if (manageUserScore > 0) {
//                    int resultUserScore = currentUserScore - manageUserScore;
//                    if (resultUserScore <= 0) {
//                        throw new CustomException(CommonErrorType.INVALID_POINT_MANAGE);
//                    } else {
//                        pointMaster.setUserScore(currentUserScore - manageUserScore);
//                        changeUpLevel(pointMaster, resultUserScore, currentUserLevel);
//                    }
//                }
//            }
//            //TODO: 관리자가 지정한 point에도 유효기간을 준다면 expiredAt으로 설정
//            PointHistory pointHistory = PointHistory.builder()
//                    .pointMasterId(pointMaster.getId())
//                    .pointType(pointManageDto.getPointManageType().equals(PointManageType.PROVIDE.getCode()) ? PointType.ADMIN_PROVIDE : PointType.ADMIN_RECEIVE)
//                    .changePoint(pointManageDto.getManageAvailablePoint())
//                    .isManual(true)
//                    .expiredAt(null)
//                    .build();
//            pointHistoryRepository.save(pointHistory);
//
//        } catch (CustomException e) {
//            return false;
//        }
//        return true;
//    }
//
//}
