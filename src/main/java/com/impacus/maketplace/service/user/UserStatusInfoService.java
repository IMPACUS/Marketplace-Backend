package com.impacus.maketplace.service.user;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.user.UserStatusInfo;
import com.impacus.maketplace.repository.user.UserStatusInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStatusInfoService {

    private final UserStatusInfoRepository userStatusInfoRepository;

    /**
     * UserStatusInfo 생성 함수
     *
     * @param userId
     */
    @Transactional
    public void addUserStatusInfo(Long userId) {
        UserStatusInfo userStatusInfo = UserStatusInfo.toEntity(userId);
        userStatusInfoRepository.save(userStatusInfo);
    }

    public UserStatusInfo findUserStatusInfoByUserId(Long userId) {
        return userStatusInfoRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(CommonErrorType.NOT_EXISTED_EMAIL));
    }

    @Transactional
    public void updateUserStatus(
            Long userId,
            UserStatus userStatus,
            String statusResponse
    ) {
        userStatusInfoRepository.updateUserStatus(userId, userStatus, statusResponse);
    }
}
