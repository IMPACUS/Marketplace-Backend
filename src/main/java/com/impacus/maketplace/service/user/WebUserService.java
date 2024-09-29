package com.impacus.maketplace.service.user;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.dto.user.response.WebUserDTO;
import com.impacus.maketplace.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebUserService {
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

    /**
     * 회원 목록 조회
     *
     * @param pageable
     * @param userName
     * @param phoneNumber
     * @param startAt
     * @param endAt
     * @param oauthProviderType
     * @param status
     * @return
     */
    public Page<WebUserDTO> getUsers(
            Pageable pageable,
            String userName,
            String phoneNumber,
            LocalDate startAt,
            LocalDate endAt,
            OauthProviderType oauthProviderType,
            UserStatus status
    ) {
        try {
            return userRepository.getUsers(pageable, userName, phoneNumber, startAt, endAt, oauthProviderType, status);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
