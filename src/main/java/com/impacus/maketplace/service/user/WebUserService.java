package com.impacus.maketplace.service.user;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.user.request.UpdateUserDTO;
import com.impacus.maketplace.dto.user.response.ReadUserSummaryDTO;
import com.impacus.maketplace.dto.user.response.WebUserDTO;
import com.impacus.maketplace.dto.user.response.WebUserDetailDTO;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.repository.user.UserStatusInfoRepository;
import com.impacus.maketplace.service.AttachFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebUserService {
    private final UserRepository userRepository;
    private final AttachFileService attachFileService;
    private final UserStatusInfoRepository userStatusInfoRepository;

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

    /**
     * 회원 정보 상세 조회 조회
     *
     * @return
     */
    public WebUserDetailDTO getUser(Long userId) {
        try {
            WebUserDetailDTO dto = userRepository.getUser(userId);
            if (dto == null) {
                throw new CustomException(UserErrorType.NOT_EXISTED_USER);
            }

            dto.updateLoginInfo();
            return dto;
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * 소비자 정보 수정
     *
     * @param userId
     * @param profileImage
     * @param dto
     */
    @Transactional
    public void updateUser(Long userId, MultipartFile profileImage, UpdateUserDTO dto) {
        try {
            Optional<Long> profileImageIdOptional = userRepository.findProfileImageIdByUserId(
                    userId);
            Optional<UserStatus> userStatusOptional = userStatusInfoRepository.findProfileImageIdByUserId(userId);

            if (userStatusOptional.isEmpty()) {
                throw new CustomException(UserErrorType.NOT_EXISTED_USER);
            }

            // 1. 프로필 이미지 업데이트
            Long profileImageId = null;
            if (profileImageIdOptional.isEmpty()) {
                if (profileImage != null) {
                    profileImageId = attachFileService.uploadFileAndAddAttachFile(
                            profileImage,
                            DirectoryConstants.PROFILE_IMAGE_DIRECTORY
                    ).getId();
                }

            } else if (profileImageIdOptional.isPresent()) {
                profileImageId = profileImageIdOptional.get();

                if (profileImage == null) {
                    attachFileService.deleteAttachFile(profileImageIdOptional.get());
                } else {
                    attachFileService.updateAttachFile(profileImageId, profileImage, DirectoryConstants.PROFILE_IMAGE_DIRECTORY);
                }
            }

            // 2. 프로필 데이터 변경
            userRepository.updateUser(userId, dto, profileImageId);

            // 3. TODO status 가 변경된 경우 로그아웃 처리
            if (userStatusOptional.get() != dto.getUserStatus()) {

            }
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }
}
