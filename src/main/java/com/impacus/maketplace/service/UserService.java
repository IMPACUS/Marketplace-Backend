package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.dto.auth.request.EmailVerificationRequest;
import com.impacus.maketplace.dto.user.request.LoginDTO;
import com.impacus.maketplace.dto.user.request.SignUpDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.redis.entity.EmailVerificationCode;
import com.impacus.maketplace.redis.entity.LoginFailAttempt;
import com.impacus.maketplace.redis.service.EmailVerificationCodeService;
import com.impacus.maketplace.redis.service.LoginFailAttemptService;
import com.impacus.maketplace.repository.UserRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final EmailVerificationCodeService emailVerificationCodeService;


    @Transactional
    public UserDTO addUser(SignUpDTO signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사 -> redis 전에도 안됬는지 확인
            User existedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
            if (existedUser != null) {
                if (existedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                    throw new CustomException(CommonErrorType.DUPLICATED_EMAIL);
                } else {
                    throw new CustomException(CommonErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
                }
            }

            // 2. 비밃번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(CommonErrorType.INVALID_PASSWORD);
            }

            // 3. User 데이터 생성 및 저장
            User user = new User(StringUtils.createStrEmail(email, OauthProviderType.NONE),
                    encodePassword(password),
                    signUpRequest.getName());
            userRepository.save(user);


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
    public List<User> findUsersByEmailAboutAllProvider(String email) {
        return userRepository.findByEmailLike("%_" + email);
    }

    /**
     * 전달한 email 과 Oauth provider 로 등록된 User를 검색하는 함수
     *
     * @param email        '%@%.%' 포맷의 이메일 데이터
     * @param providerType 찾을 OauthProviderType
     * @return 요청한 데이터 기준으로 데이터가 존재하는 경우 User, 데이터가 존재하지 않은 경우 null
     */
    public User findUserByEmailAndOauthProviderType(String email, OauthProviderType providerType) {
        List<User> userList = findUsersByEmailAboutAllProvider(email);
        List<User> findUserList = userList.stream()
                .filter(user -> user.getEmail().equals(providerType + "_" + email))
                .toList();

        return (!findUserList.isEmpty()) ? findUserList.get(0) : null;
    }

    @Transactional(noRollbackFor = CustomException.class)
    public UserDTO login(LoginDTO loginRequest, UserType userType) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사
            User user = validateAndFindUser(email, userType);

            // 2. 비밃번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(CommonErrorType.INVALID_PASSWORD);
            }

            // 3. 비밀번호 확인
            if (!passwordEncoder.matches(password, user.getPassword())) {
                LoginFailAttempt loginFailAttempt = loginFailAttemptService.increaseLoginCnt(user);

                if (loginFailAttempt.getFailAttemptCnt() > LIMIT_LOGIN_FAIL_ATTEMPT) {
                    changeUserStatus(user, UserStatus.BLOCKED);
                }
                throw new CustomException(CommonErrorType.WRONG_PASSWORD);
            } else {
                loginFailAttemptService.resetLoginFailAttempt(user);
            }

            // 4. JWT 토큰 생성
            TokenInfoVO tokenInfoVO = getJwtTokenInfo(user.getEmail(), password);

            // 5. 최근 로그인 시간 갱신
            updateRecentLoginAt(user);

            return new UserDTO(user, tokenInfoVO);
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
    private User validateAndFindUser(String email, UserType userType) {
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

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new CustomException(CommonErrorType.BLOCKED_EMAIL);
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
            throw new CustomException(CommonErrorType.NOT_EXISTED_EMAIL);
        } else {
            if (!checkedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                throw new CustomException(CommonErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
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
            case BLOCKED: {
                userRepository.updateUserStatus(user.getId(), UserStatus.BLOCKED,
                        "로그인 시도 가능 횟수 초과");
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

        return userRepository.findByEmail(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_EMAIL));
    }

    public boolean existUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    @Transactional
    public void findFistDormancyUser() {
        LocalDateTime fiveMonthAgo = LocalDateTime.now().minusMonths(5).plusDays(1).truncatedTo(ChronoUnit.DAYS);
        List<User> firstDormancyUser = userRepository.findByRecentLoginAtBeforeAndFirstDormancyIsFalse(fiveMonthAgo);
        LocalDate updateDormancyAt = LocalDateTime.now().plusMonths(1).toLocalDate();

        for (User user : firstDormancyUser) {
            user.setDormancyMonths(5);
            user.setFirstDormancy(true);
            user.setUpdateDormancyAt(updateDormancyAt);

            int underscoreIndex = user.getEmail().indexOf("_") + 1;
            String realUserEmail = user.getEmail().substring(underscoreIndex);

            EmailDto emailDto = EmailDto.builder()
                    .subject(MailType.POINT_REDUCTION.getSubject())
                    .receiveEmail(realUserEmail)
                    .build();
            emailService.sendMail(emailDto, MailType.POINT_REDUCTION);
        }
    }

    /**
     * 이메일 인증 요청 이메일을 보내는 함수
     *
     * @param email 인증 요청 이메일
     */
    public void sendVerificationCodeToEmail(String email) {
        try {
            // 1. 이메일 전송
            String code = emailService.sendEmailVerificationMail(email);

            // 2. 이메일 인증 코드 저장
            emailVerificationCodeService.saveEmailVerificationCode(email, code);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 이메일 인증 확인 결과를 전달하는 API
     *
     * @param request
     */
    public boolean confirmEmail(EmailVerificationRequest request) {
        String email = request.getEmail();
        String code = request.getCode();

        try {
            EmailVerificationCode emailVerificationCode = emailVerificationCodeService
                    .findEmailVerificationCodeByEmailAndCode(email, code);
            if (emailVerificationCode != null) {
                emailVerificationCodeService.deleteEmailVerificationCode(emailVerificationCode);
            }

            return emailVerificationCode != null;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * User 저장하는 함수
     *
     * @param user
     */
    public void saveUser(User user) {
        userRepository.save(user);
    }

    /**
     * id로 판매자를 조회하는 함수
     *
     * @param id
     * @return
     */
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_EMAIL));
    }

    /**
     * userType을 업데이트하는 합수
     *
     * @param user
     * @param userType
     */
    @Transactional
    public void updateUserType(User user, UserType userType) {
        user.setType(userType);
        userRepository.save(user);
    }

}
