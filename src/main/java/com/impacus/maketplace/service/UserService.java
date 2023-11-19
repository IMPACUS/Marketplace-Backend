package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.enumType.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.entity.User;
import com.impacus.maketplace.entity.dto.user.UserDTO;
import com.impacus.maketplace.entity.dto.user.request.LoginRequest;
import com.impacus.maketplace.entity.dto.user.request.SignUpRequest;
import com.impacus.maketplace.entity.vo.auth.TokenInfoVO;
import com.impacus.maketplace.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.CustomUserDetails;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder managerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserDTO addUser(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        // 1. 이메일 유효성 검사
        User existedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
        if (existedUser != null) {
            if (existedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                throw new CustomException(ErrorType.DUPLICATED_EMAIL);
            } else {
                throw new CustomException(ErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }

        // 2. 비밃번호 유효성 검사
        if (!StringUtils.checkPasswordValidation(password)) {
            throw new CustomException(ErrorType.INVALID_PASSWORD);
        }

        // 3. User 데이터 생성 및 저장
        User user = new User(StringUtils.createStrEmail(email, OauthProviderType.NONE),
            encodePassword(password),
            signUpRequest.getName());
        userRepository.save(user);

        // 4. UserDTO 반환
        return new UserDTO(user);
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

    public UserDTO login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

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
        if (!StringUtils.checkPasswordValidation(password)) {
            throw new CustomException(ErrorType.INVALID_PASSWORD);
        }

        // 3. 비밀번호 확인
//        if (!encodePassword(password).equals(user.getPassword())) {
//            increaseLoginCnt(user);
//            throw new CustomException(ErrorType.WRONG_PASSWORD);
//        }

        // 4. JWT 토큰 생성
        TokenInfoVO tokenInfoVO = getJwtTokenInfo(user.getEmail(), password);

        return new UserDTO(user, tokenInfoVO);
    }

    /**
     * 로그인을 요청한 사용자의 비밀번호가 틀린 경우, 로그인 시도 횟수 추가를 진행하는 함수
     *
     * @param user
     */
    private void increaseLoginCnt(User user) {
        // redis 설정
        // 비밀번호 증가 로직 설정
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
        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        }

        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String email = user.getEmail();

        return userRepository.findByEmail(email);
    }


}
