package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.exception.Custom400Exception;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.entity.User;
import com.impacus.maketplace.entity.dto.user.UserDTO;
import com.impacus.maketplace.entity.dto.user.request.SignUpRequest;
import com.impacus.maketplace.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserDTO addUser(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        // 1. 이메일 유효성 검사
        User existedUser = findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
        if (existedUser != null) {
            if (existedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                throw new Custom400Exception(ErrorType.DUPLICATED_EMAIL);
            } else {
                throw new Custom400Exception(ErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
            }
        }

        // 2. 비밃번호 유효성 검사
        if (!StringUtils.checkPasswordValidation(password)) {
            throw new Custom400Exception(ErrorType.INVALID_PASSWORD);
        }

        // 3. User 데이터 생성 및 저장
        User user = new User(StringUtils.createStrEmail(email, OauthProviderType.NONE), password,
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
}
