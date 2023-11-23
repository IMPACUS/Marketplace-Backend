package com.impacus.maketplace.redis.service;

import com.impacus.maketplace.entity.User;
import com.impacus.maketplace.redis.entity.LoginFailAttempt;
import com.impacus.maketplace.redis.repository.LoginFailAttemptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginFailAttemptService {

    private final LoginFailAttemptRepository loginFailAttemptRepository;


    /**
     * 로그인을 요청한 사용자의 비밀번호가 틀린 경우, 로그인 시도 횟수 추가를 진행하는 함수
     *
     * @param user
     */
    @Transactional
    public LoginFailAttempt increaseLoginCnt(User user) {
        String email = user.getEmail();

        // 1. 이전 로그인 실패 횟수 찾기
        LoginFailAttempt loginFailAttempt = findLoginFailAttemptByEmail(email);
        if (loginFailAttempt == null) {
            loginFailAttempt = addLoginFailAttempt(email);
        }

        // 2. login 횟수 증가
        loginFailAttempt.increaseFailAttemptCnt();
        loginFailAttemptRepository.save(loginFailAttempt);

        return loginFailAttempt;
    }

    public LoginFailAttempt findLoginFailAttemptByEmail(String email) {
        return loginFailAttemptRepository.findByEmail(email)
            .orElse(null);
    }

    public LoginFailAttempt addLoginFailAttempt(String email) {
        LoginFailAttempt loginFailAttempt = new LoginFailAttempt(email);
        loginFailAttemptRepository.save(loginFailAttempt);

        return loginFailAttempt;
    }

}
