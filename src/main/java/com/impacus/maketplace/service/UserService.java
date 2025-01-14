package com.impacus.maketplace.service;

import com.impacus.maketplace.common.constants.SMSContentsConstants;
import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.point.PointType;
import com.impacus.maketplace.common.enumType.point.RewardPointType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.EmailDTO;
import com.impacus.maketplace.dto.admin.request.AdminLoginDTO;
import com.impacus.maketplace.dto.auth.CertificationResult;
import com.impacus.maketplace.dto.auth.request.EmailVerificationDTO;
import com.impacus.maketplace.dto.auth.request.SMSVerificationForEmailDTO;
import com.impacus.maketplace.dto.auth.request.SMSVerificationForPasswordDTO;
import com.impacus.maketplace.dto.auth.request.SMSVerificationRequestDTO;
import com.impacus.maketplace.dto.auth.response.SMSVerificationForEmailResultDTO;
import com.impacus.maketplace.dto.user.CommonUserDTO;
import com.impacus.maketplace.dto.user.ConsumerEmailDTO;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.request.SignUpDTO;
import com.impacus.maketplace.dto.user.response.CheckExistedEmailDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.consumer.Consumer;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.entity.user.UserStatusInfo;
import com.impacus.maketplace.redis.entity.LoginFailAttempt;
import com.impacus.maketplace.redis.entity.VerificationCode;
import com.impacus.maketplace.redis.service.LoginFailAttemptService;
import com.impacus.maketplace.redis.service.VerificationCodeService;
import com.impacus.maketplace.repository.consumer.ConsumerRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.admin.AdminService;
import com.impacus.maketplace.service.alarm.user.AlarmUserService;
import com.impacus.maketplace.service.common.sms.SMSService;
import com.impacus.maketplace.service.oauth.OAuthServiceFactory;
import com.impacus.maketplace.service.point.PointService;
import com.impacus.maketplace.service.point.greenLabelPoint.GreenLabelPointAllocationService;
import com.impacus.maketplace.service.user.UserStatusInfoService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private static final int LIMIT_LOGIN_FAIL_ATTEMPT = 5;

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final LoginFailAttemptService loginFailAttemptService;
    private final EmailService emailService;
    private final VerificationCodeService verificationCodeService;
    private final AdminService adminService;
    private final UserStatusInfoService userStatusInfoService;
    private final PointService pointService;
    private final GreenLabelPointAllocationService greenLabelPointAllocationService;
    private final AlarmUserService alarmUserService;
    private final ConsumerRepository consumerRepository;
    private final SMSService smsService;
    private final OAuthServiceFactory oAuthServiceFactory;

    @Transactional
    public UserDTO addUser(SignUpDTO signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사 -> redis 전에도 안됬는지 확인
            User existedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
            if (existedUser != null) {
                if (existedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                    throw new CustomException(UserErrorType.DUPLICATED_EMAIL);
                } else {
                    throw new CustomException(UserErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
                }
            }
            // 탈퇴 14일 이내 회원 확인
            if (!canRejoin(email)) {
                throw new CustomException(UserErrorType.FAIL_TO_REJOIN_14);
            }

            // 2. 비밀번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(UserErrorType.INVALID_PASSWORD);
            }

            // 3. User&UserStatus 생성 및 저장
            User user = new User(
                    email,
                    OauthProviderType.NONE,
                    password,
                    signUpRequest.getName());
            userRepository.save(user);
            userStatusInfoService.addUserStatusInfo(user.getId());
            alarmUserService.saveDefault(user.getId());

            // 4. 포인트 관련 Entity 생성
            // (LevelPointMaster, LevelAchievement, GreenLabelPoint)
            pointService.addEntityAboutPoint(user.getId());

            // 5. UserDTO 반환
            return new UserDTO(user);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }


    /**
     * Oauth Provider와 상관없이 email로 등록된 User를 검색하는 함수
     *
     * @param email '%@%.%' 포맷의 이메일 데이터
     * @return 매개변수로 받은 email로 등록된 User 리스트
     */
    public Optional<User> findUsersByEmailAboutAllProvider(String email) {
        return userRepository.findByEmailLikeAndIsDeletedFalse("%_" + email);
    }

    /**
     * email로 재가입 가능 여부를 확인하는 함수
     *
     * @param email
     * @return
     */
    public boolean canRejoin(String email) {
        Optional<User> userOptional = userRepository.findByEmailLikeAndIsDeletedTrue("%_" + email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.isRejoinable();
        }

        return true;
    }

    /**
     * 전달한 email 과 Oauth provider 로 등록된 User를 검색하는 함수
     *
     * @param email        '%@%.%' 포맷의 이메일 데이터
     * @param providerType 찾을 OauthProviderType
     * @return 요청한 데이터 기준으로 데이터가 존재하는 경우 User, 데이터가 존재하지 않은 경우 null
     */
    public User findUserByEmailAndOauthProviderType(String email, OauthProviderType providerType) {
        Optional<User> userOptional = findUsersByEmailAboutAllProvider(email);
        if (userOptional.isPresent()) {
            if (userOptional.get().getEmail().equals(providerType + "_" + email)) {
                return userOptional.get();
            }
        }

        return null;
    }

    /**
     * (소비자, 판매자) 로그인 함수
     *
     * @param loginRequest
     * @param userType
     * @return
     */
    @Transactional(noRollbackFor = CustomException.class)
    public UserDTO login(LoginDTO loginRequest, UserType userType) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사
            User user = validateAndFindUser(email, userType);

            // 2. 비밃번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(UserErrorType.INVALID_PASSWORD);
            }

            // 3. 비밀번호 확인
            // 3-1. 틀린 경우: 틀린 횟수 추가
            // 3-2 맞는 경우: 이전에 틀렸던 횟수 초기화
            if (!passwordEncoder.matches(password, encodePassword(user.getPassword()))) {
                LoginFailAttempt loginFailAttempt = loginFailAttemptService.increaseLoginCnt(user);
                throw new CustomException(UserErrorType.WRONG_PASSWORD);
            } else {
                loginFailAttemptService.resetLoginFailAttempt(user);
            }

            // 4. JWT 토큰 생성
            TokenInfoVO tokenInfoVO = getJwtTokenInfo(user.getEmail(), password);

            // 5. 최근 로그인 시간 갱신
            updateRecentLoginAt(user);

            // 6. 소비자인 경우 출석 포인트 지급
            if (userType == UserType.ROLE_CERTIFIED_USER) {
                greenLabelPointAllocationService.payGreenLabelPoint(
                        user.getId(),
                        PointType.CHECK,
                        RewardPointType.CHECK.getAllocatedPoints()
                );
            }

            return new UserDTO(user, tokenInfoVO);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * (관리자) 로그인 함수
     *
     * @param dto
     * @return
     */
    @Transactional(noRollbackFor = CustomException.class)
    public UserDTO login(AdminLoginDTO dto) {
        String adminIdName = dto.getAdminIdName();
        String password = dto.getPassword();

        try {
        // 1. 이메일 유효성 검사
        AdminInfo admin = validateAndFindAdmin(adminIdName);

        // 2. 비밀번호 유효성 검사
        if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
            throw new CustomException(UserErrorType.INVALID_PASSWORD);
        }

        // TODO User 설계 마무리된 후 수정할 예정
//
//            // 3. 비밀번호 확인
//            // 3-1. 틀린 경우: 틀린 횟수 추가
//            // 3-2 맞는 경우: 이전에 틀렸던 횟수 초기화
//            if (!passwordEncoder.matches(password, encodePassword(admin.getPassword()))) {
//                LoginFailAttempt loginFailAttempt = loginFailAttemptService.increaseLoginCnt(admin);
//                throw new CustomException(CommonErrorType.WRONG_PASSWORD);
//            } else {
//                loginFailAttemptService.resetLoginFailAttempt(user);
//            }

        // 4. JWT 토큰 생성
        TokenInfoVO tokenInfoVO = getJwtTokenInfo(admin.getAdminIdName(), password);

        return new UserDTO(admin, tokenInfoVO);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 이메일 유효성 검사를 한 후, 유효성 검사에 통과한 경우, User를 반환하는 함수
     *
     * @param email
     * @param userType
     * @return
     */
    public User validateAndFindUser(String email, UserType userType) {
        User user = switch (userType) {
            case ROLE_CERTIFIED_USER -> {
                User checkedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
                validateCertifiedUser(checkedUser);

                yield checkedUser;
            }
            case ROLE_APPROVED_SELLER -> {
                User checkedSeller = findUserByEmail(email);
                validateApprovedSeller(checkedSeller);

                yield checkedSeller;
            }
            default -> throw new CustomException(HttpStatus.FORBIDDEN, CommonErrorType.ACCESS_DENIED_ACCOUNT);
        };

        UserStatusInfo userStatusInfo = userStatusInfoService.findUserStatusInfoByUserId(user.getId());
        switch (userStatusInfo.getStatus()) {
            case DEACTIVATED -> throw new CustomException(UserErrorType.DEACTIVATED_USER);
            case SUSPENDED -> throw new CustomException(UserErrorType.SUSPENDED_USER);
        }

        return user;
    }

    /**
     * User 유효성 검사를 하는 함수 (사용자인 경우)
     *
     * @param checkedUser
     */
    private void validateCertifiedUser(User checkedUser) {
        if (checkedUser == null) {
            throw new CustomException(UserErrorType.NOT_EXISTED_EMAIL);
        } else {
            if (!checkedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                throw new CustomException(UserErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }
    }

    /**
     * User 유효성 검사를 하는 함수 (판매자인 경우)
     *
     * @param checkedSeller
     */
    private void validateApprovedSeller(User checkedSeller) {
        if (checkedSeller.getType() != UserType.ROLE_APPROVED_SELLER) {
            throw new CustomException(HttpStatus.FORBIDDEN, CommonErrorType.ACCESS_DENIED_ACCOUNT);
        }
    }

    /**
     * 이메일 유효성 검사를 한 후, 유효성 검사에 통과한 경우, User를 반환하는 함수
     *
     * @param adminIdName
     * @return
     */
    private AdminInfo validateAndFindAdmin(String adminIdName) {
        AdminInfo admin = adminService.findAdminInfoBYAdminIdName(adminIdName);

        // TODO User 관련 설계 마무리 후 수정 예정
//        if (user.getStatus() == UserStatus.BLOCKED) {
//            throw new CustomException(CommonErrorType.BLOCKED_EMAIL);
//        }

        return admin;
    }

    /**
     * 최근 로그인한 시간을 업데이트하는 함수
     *
     * @param user
     */
    @Transactional
    public void updateRecentLoginAt(User user) {
        user.setRecentLoginAt();
        userRepository.save(user);
    }

    @Transactional(noRollbackFor = CustomException.class)
    public void changeUserStatus(User user, UserStatus userStatus) {
        switch (userStatus) {
            case SUSPENDED: {
                userStatusInfoService.updateUserStatus(user.getId(), UserStatus.SUSPENDED, "로그인 시도 가능 횟수 초과");
            }
            break;
        }
    }

    public TokenInfoVO getJwtTokenInfo(String email, String password) {
        // 1. Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email, password);

        // 2. 실제 검증 진행
        Authentication authentication = managerBuilder.getObject()
                .authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return tokenProvider.createToken(authentication);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * securityContext에 저장된 User 정보를 가져오는 함수
     *
     * @return
     */
    public Optional<User> getMyUserWithAuthorities(String accessToken) {
        accessToken = StringUtils.parseGrantTypeInToken("Bearer", accessToken);

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String email = user.getEmail();

        return userRepository.findByEmailAndIsDeletedFalse(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_EMAIL));
    }

    public CommonUserDTO findCommonUserByEmail(String email) {
        return userRepository.findCommonUserByEmail(email);
    }

    public boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 이메일 인증 요청 이메일을 보내는 함수
     *
     * @param email 인증 요청 이메일
     */
    public void sendVerificationCodeToEmail(String email, UserType role) {
        try {
            // 1. 이메일 전송
            String code = emailService.sendEmailVerificationMail(email, role);

            // 2. 이메일 인증 코드 저장
            verificationCodeService.saveVerificationCode(email, code);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 이메일 인증 확인 결과를 전달하는 API
     *
     * @param request
     */
    public boolean confirmEmail(EmailVerificationDTO request) {
        String email = request.getEmail();
        String code = request.getCode();

        try {
            VerificationCode emailVerificationCode = verificationCodeService
                    .findVerificationCode(email, code);
            if (emailVerificationCode != null) {
                verificationCodeService.deleteIdentifierVerificationCode(emailVerificationCode);
            }

            return emailVerificationCode != null;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * User 저장하는 함수 (판매자)
     *
     * @param user
     */
    @Transactional
    public void saveUser(User user) {
        // 1. User 생성
        User newUser = userRepository.save(user);

        // 2. UserStatus 생성
        userStatusInfoService.addUserStatusInfo(newUser.getId());
    }

    /**
     * id로 판매자를 조회하는 함수
     *
     * @param id
     * @return
     */
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_EMAIL));
    }

    /**
     * userType을 업데이트하는 합수
     *
     * @param userId
     * @param userType
     */
    @Transactional
    public void updateUserType(Long userId, UserType userType) {
        userRepository.updateUserType(userId, userType);
    }

    public CheckExistedEmailDTO checkExistedEmailForSeller(String email) {
        boolean isExited = existUserByEmail(email);
        return CheckExistedEmailDTO.toDTO(isExited);
    }



    /**
     * 사용자 개인 정보 저장
     *
     * @param userId 사용자 ID
     * @param certificationResult 본인 인증 데이터
     */
    @Transactional
    public void saveCertification(Long userId, CertificationResult certificationResult) {
        // 1. 사용자 존재하는지 확인
        if (!userRepository.existsByIdAndType(
                userId,
                List.of(UserType.ROLE_CERTIFIED_USER, UserType.ROLE_UNCERTIFIED_USER)
        )) {
            throw new CustomException(UserErrorType.NOT_EXISTED_USER);
        }

        // 2. 이미 존재하는 핸드폰 번호인지 확인
        if (userRepository.existsConsumerByPhoneNumberAndUserId(
                userId,
                certificationResult.getMobileNo())
        ) {
            throw new CustomException(UserErrorType.DUPLICATED_PHONE_NUMBER);
        }

        // 2. 저장 혹은 업데이트
        userRepository.saveOrUpdateCertification(userId, certificationResult);
        saveOrUpdateConsumer(userId, certificationResult);
    }

    /**
     * Consumer 정보 저장 혹은 업데이트
     *
     * @param userId 사용자 ID
     * @param certificationResult 본인 인증 데이터
     */
    @Transactional
    public void saveOrUpdateConsumer(Long userId, CertificationResult certificationResult) {
        // 회원가입 가능한 CI 인지 확인(+ 탈퇴한 회원의 CI 인지 확인)
        validateUserRejoinable(certificationResult.getCi());

        if (consumerRepository.existsByUserId(userId)) { // 업데이트
            consumerRepository.updateConsumer(userId, certificationResult.getCi(), LocalDateTime.now());
        } else { // 생성
            Consumer consumer = certificationResult.toEntity(userId);
            consumerRepository.save(consumer);
        }
    }

    private void validateUserRejoinable(String ci) {
        User user = userRepository.findUserByCI(ci);
        if (user == null) {
            return;
        }

        if (user.isDeleted()) {
            if (!user.isRejoinable()) {
                throw new CustomException(UserErrorType.FAIL_TO_REJOIN_14);
            }
        } else {
            throw new CustomException(UserErrorType.DUPLICATED_CI);
        }
    }

    /**
     * @param dto
     */
    @Transactional
    public void sendVerificationCodeToSMS(SMSVerificationRequestDTO dto) {
        try {
            String code = StringUtils.generateRandomCode();
            String incomingNumber = StringUtils.extractPhoneNumber(dto.getPhoneNumber());

            // 1. SMS 전송
            boolean result = smsService.sendSimpleSMS(incomingNumber,
                    String.format(SMSContentsConstants.VERIFICIATION, code)
            );
            if (!result) {
                throw new CustomException(CommonErrorType.FAIL_TO_SEND_SMS);
            } else {
                // 2. 코드 저장
                verificationCodeService.saveVerificationCode(dto.getPhoneNumber(), code);
            }
        } catch (CustomException e) {
            throw new CustomException(e);
        }
    }

    /**
     * 이메일 찾기를 위한 휴대폰 인증 확인
     *
     * @param dto
     * @return
     */
    public SMSVerificationForEmailResultDTO verifySMSCodeForEmail(SMSVerificationForEmailDTO dto) {
        try {
            String phoneNumber = dto.getPhoneNumber();

            // 코드 확인
            VerificationCode verificationCode = verificationCodeService
                    .findVerificationCode(phoneNumber, dto.getCode());
            if (verificationCode != null) {
                verificationCodeService.deleteIdentifierVerificationCode(verificationCode);
            } else {
                return SMSVerificationForEmailResultDTO.toDTO(false);
            }

            // 사용자 확인
            ConsumerEmailDTO consumer = userRepository.findConsumerByPhoneNumber(phoneNumber);
            if (consumer == null) {
                throw new CustomException(UserErrorType.NOT_EXISTED_USER);
            }

            return SMSVerificationForEmailResultDTO.toDTO(true, consumer.getEmail());
        } catch (Exception e) {
            throw new CustomException(e);
        }
    }

    /**
     * 비밀번호 찾기를 위한 휴대폰 인증 확인
     *
     * @param dto
     * @return
     */
    public boolean verifySMSCodeForPassword(SMSVerificationForPasswordDTO dto) {
        String phoneNumber = dto.getPhoneNumber();
        String email = dto.getEmail();

        // 코드 확인
//        VerificationCode verificationCode = verificationCodeService
//                .findVerificationCode(phoneNumber, dto.getCode());
//        if (verificationCode != null) {
//            verificationCodeService.deleteIdentifierVerificationCode(verificationCode);
//        } else {
//            return false;
//        }

        // 사용자 확인
        ConsumerEmailDTO consumer = userRepository.findConsumerByPhoneNumberAndEmail(phoneNumber, email);
        if (consumer == null) {
            throw new CustomException(UserErrorType.NOT_EXISTED_USER);
        }

        // 이메일 전송
        HashMap<String, String> mailHash = new HashMap<>();
        mailHash.put("password", consumer.getPassword());
        EmailDTO emailDTO = EmailDTO.toDTO(email, MailType.PASSWORD, mailHash);
        emailService.sendSingleEmail(emailDTO);

        return true;
    }
}
