package com.impacus.maketplace.repository.user.querydsl;

import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserLevel;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.dto.common.request.IdsDTO;
import com.impacus.maketplace.dto.user.CommonUserDTO;
import com.impacus.maketplace.dto.user.request.UpdateUserDTO;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.dto.user.response.WebUserDTO;
import com.impacus.maketplace.dto.user.response.WebUserDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface UserCustomRepository {
    ReadUserSummaryDTO findUserSummaryByEmail(String email);

    /**
     * 레벨 등급에 해당하는 사용자 id 리스트 반환
     *
     * @param userLevel
     * @return
     */
    List<Long> findUserIdByUserLevel(UserLevel userLevel);

    void deleteConsumer(Long userId);

    Page<WebUserDTO> getUsers(
            Pageable pageable,
            String userName,
            String phoneNumber,
            LocalDate startAt,
            LocalDate endAt,
            OauthProviderType oauthProviderType,
            UserStatus status
    );

    WebUserDetailDTO getUser(Long userId);

    long updateUser(Long userId, UpdateUserDTO dto, Long profileImageId);

    CommonUserDTO findCommonUserByEmail(String email);

    List<WebUserDTO> findUsersByIds(
            IdsDTO dto
    );
}
