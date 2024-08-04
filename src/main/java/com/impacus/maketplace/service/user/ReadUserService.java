package com.impacus.maketplace.service.user;

import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadUserService {
    private final UserRepository userRepository;

    /**
     * 이메일로 사용자 프로필 검색하는 함수
     *
     * @param email 사용자를 검색하려고 하는 이메일
     * @return
     */
    public ReadUserSummaryDTO getUserSummary(String email) {
        try {
            return userRepository.findUserSummaryByEmail(email);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
