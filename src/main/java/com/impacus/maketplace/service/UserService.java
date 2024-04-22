package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.MailType;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.dto.EmailDto;
import com.impacus.maketplace.dto.seller.request.SellerRequest;
import com.impacus.maketplace.dto.seller.response.SellerDTO;
import com.impacus.maketplace.dto.user.request.LoginRequest;
import com.impacus.maketplace.dto.user.request.SignUpRequest;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.common.AttachFile;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.redis.entity.EmailVerificationCode;
import com.impacus.maketplace.redis.entity.LoginFailAttempt;
import com.impacus.maketplace.redis.service.EmailVerificationCodeService;
import com.impacus.maketplace.redis.service.LoginFailAttemptService;
import com.impacus.maketplace.repository.UserRepository;
import com.impacus.maketplace.service.seller.SellerAdjustmentInfoService;
import com.impacus.maketplace.service.seller.SellerBusinessInfoService;
import com.impacus.maketplace.service.seller.SellerService;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import security.CustomUserDetails;

import java.io.IOException;
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
    private static final int FILE_LIMIT = 5242880;
    private static final int LOGO_IMAGE_LIMIT = 187500;
    private static final String LOGO_IMAGE_DIRECTORY = "logoImage";
    private static final String COPY_FILE_DIRECTORY = "copyFile";

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final LoginFailAttemptService loginFailAttemptService;
    private final EmailService emailService;
    private final EmailVerificationCodeService emailVerificationCodeService;
    private final AttachFileService attachFileService;
    private final SellerService sellerService;
    private final SellerBusinessInfoService sellerBusinessInfoService;
    private final SellerAdjustmentInfoService sellerAdjustmentInfoService;

    @Transactional
    public UserDTO addUser(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사 -> redis 전에도 안됬는지 확인
            User existedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
            if (existedUser != null) {
                if (existedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                    throw new CustomException(ErrorType.DUPLICATED_EMAIL);
                } else {
                    throw new CustomException(ErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
                }
            }

            // 2. 비밃번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(ErrorType.INVALID_PASSWORD);
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
    public UserDTO login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사
            User user = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
            if (user == null) {
                throw new CustomException(ErrorType.NOT_EXISTED_EMAIL);
            } else {
                if (!user.getEmail().contains(OauthProviderType.NONE.name())) {
                    throw new CustomException(ErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
                } else {
                    if (user.getStatus() == UserStatus.BLOCKED) {
                        throw new CustomException(ErrorType.BLOCKED_EMAIL);
                    }
                }
            }

            // 2. 비밃번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(ErrorType.INVALID_PASSWORD);
            }

            // 3. 비밀번호 확인
            if (!passwordEncoder.matches(password, user.getPassword())) {
                LoginFailAttempt loginFailAttempt = loginFailAttemptService.increaseLoginCnt(user);

                if (loginFailAttempt.getFailAttemptCnt() > LIMIT_LOGIN_FAIL_ATTEMPT) {
                    changeUserStatus(user, UserStatus.BLOCKED);
                }
                throw new CustomException(ErrorType.WRONG_PASSWORD);
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

    private String encodePassword(String password) {
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
                .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_EMAIL));
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
     * @param email
     */
    public boolean confirmEmail(String email, String code) {
        try {
            EmailVerificationCode emailVerificationCode = emailVerificationCodeService.findEmailVerificationCodeByEmailAndCode(email, code);
            if (emailVerificationCode != null) {
                emailVerificationCodeService.deleteEmailVerificationCode(emailVerificationCode);
            }

            return emailVerificationCode != null;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 판매자를 생성하는 함수
     *
     * @param sellerRequest
     * @param logoImage
     * @param businessRegistrationImage
     * @param mailOrderBusinessReportImage
     * @param bankBookImage
     * @return
     */
    @Transactional
    public SellerDTO addSeller(SellerRequest sellerRequest,
                               MultipartFile logoImage,
                               MultipartFile businessRegistrationImage,
                               MultipartFile mailOrderBusinessReportImage,
                               MultipartFile bankBookImage) throws IOException {
//        try {
        String email = sellerRequest.getEmail();
        String password = sellerRequest.getPassword();

        // 1. 이메일 유효성 검사
        User existedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
        if (existedUser != null) {
            throw new CustomException(ErrorType.DUPLICATED_EMAIL);
        }

        // 2. 비밃번호 유효성 검사
        if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
            throw new CustomException(ErrorType.INVALID_PASSWORD);
        }

        // 3. 상세 데이터 유효성 검사
        if (sellerRequest.getBusinessType() == BusinessType.SIMPLIFIED_TAXABLE_PERSON) {
            if (mailOrderBusinessReportImage != null || sellerRequest.getMailOrderBusinessReportNumber() != null) {
                throw new CustomException(ErrorType.INVALID_REQUEST_DATA, "간이 과세자인 경우, 통신판매업신고증관련 값이 null 이여야 합니다.");
            }
        } else {
            if (mailOrderBusinessReportImage == null || sellerRequest.getMailOrderBusinessReportNumber() == null) {
                throw new CustomException(ErrorType.INVALID_REQUEST_DATA, "간이 과세자가 아닌 경우, 통신판매업신고증관련 값이 null 이면 안됩니다.");
            }
        }
        validateFileLimit(logoImage, businessRegistrationImage, mailOrderBusinessReportImage, bankBookImage);

        // 4. 로고 이미지, 사업자 등록증 사본, 통신판매업신고증사본, 통장 사본 저장
        AttachFile logoImageFile = attachFileService.uploadFileAndAddAttachFile(logoImage, LOGO_IMAGE_DIRECTORY);
        AttachFile businessRegistrationFile = attachFileService.uploadFileAndAddAttachFile(businessRegistrationImage, COPY_FILE_DIRECTORY);
        AttachFile mailOrderBusinessReportFile = mailOrderBusinessReportImage == null ? null : attachFileService.uploadFileAndAddAttachFile(mailOrderBusinessReportImage, COPY_FILE_DIRECTORY);
        AttachFile bankBookFile = attachFileService.uploadFileAndAddAttachFile(bankBookImage, COPY_FILE_DIRECTORY);

        // 5. User 저장
        User user = new User(
                StringUtils.createStrEmail(email, OauthProviderType.NONE),
                encodePassword(password),
                email,
                sellerRequest.getContactNumber(),
                UserType.ROLE_SELLER);
        userRepository.save(user);

        // 6. Seller 저장
        Seller seller = sellerService.saveSeller(sellerRequest.toSellerEntity(user.getId(),
                logoImageFile.getId()));
        Long sellerId = seller.getId();

        // 7. SellerBusinessInfo 저장
        SellerBusinessInfo sellerBusinessInfo = sellerRequest.toSellerBusinessInfoEntity(sellerId,
                businessRegistrationFile.getId(),
                mailOrderBusinessReportFile == null ? null : mailOrderBusinessReportFile.getId());
        sellerBusinessInfoService.saveSellerBusinessInfo(sellerBusinessInfo);

        // 8. SellerAdjustmentInfo 저장
        SellerAdjustmentInfo adjustmentInfo = sellerRequest.toSellerAdjustmentInfo(sellerId, bankBookFile.getId());
        sellerAdjustmentInfoService.saveSellerAdjustmentInfo(adjustmentInfo);

        return SellerDTO.toDTO(user);
//        } catch (Exception e) {
//            throw new CustomException(e);
//        }
    }

    private void validateFileLimit(MultipartFile logoImage,
                                   MultipartFile businessRegistrationImage,
                                   MultipartFile mailOrderBusinessReportImage,
                                   MultipartFile bankBookImage) {
        if (logoImage.getSize() > LOGO_IMAGE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "로고 이미지 크기가 제한을 넘었습니다.");
        }

        if (businessRegistrationImage.getSize() > FILE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "사업자 등록증 사본의 이미지 크기가 제한을 넘었습니다.");
        }

        if (mailOrderBusinessReportImage != null && mailOrderBusinessReportImage.getSize() > FILE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "통신판매업신고증 사본의 크기가 제한을 넘었습니다.");
        }

        if (bankBookImage.getSize() > FILE_LIMIT) {
            new CustomException(ErrorType.INVALID_REQUEST_DATA, "통장 사본의 이미지 크기가 제한을 넘었습니다.");
        }

    }

}
