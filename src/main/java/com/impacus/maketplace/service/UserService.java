package com.impacus.maketplace.service;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.enumType.OauthProviderType;
import com.impacus.maketplace.common.exception.Custom400Exception;
import com.impacus.maketplace.entity.User;
import com.impacus.maketplace.entity.dto.user.UserDTO;
import com.impacus.maketplace.entity.dto.user.request.SignInRequest;
import com.impacus.maketplace.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserDTO addUser(SignInRequest signInRequest) {
        UserDTO userDTO = null;
        String email = signInRequest.getEmail();

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

        return userDTO;
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
            .filter(user -> user.getEmail() == providerType + "_" + email)
            .toList();

        return (findUserList.size() > 0) ? findUserList.get(0) : null;
    }
}
