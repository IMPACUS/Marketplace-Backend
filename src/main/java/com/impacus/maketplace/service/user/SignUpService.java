package com.impacus.maketplace.service.user;

import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.enumType.user.OauthProviderType;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.StringUtils;
import com.impacus.maketplace.dto.user.request.SignUpDTO;
import com.impacus.maketplace.dto.user.response.UserDTO;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.user.UserRepository;
import com.impacus.maketplace.service.UserService;
import com.impacus.maketplace.service.alarm.user.AlarmUserService;
import com.impacus.maketplace.service.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final UserStatusInfoService userStatusInfoService;
    private final PointService pointService;
    private final AlarmUserService alarmUserService;
    private final UserDeactivationService userDeactivationService;

    @Transactional
    public UserDTO addUser(SignUpDTO signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();

        try {
            // 1. 이메일 유효성 검사 -> redis 전에도 안됬는지 확인
            User existedUser = userService.findUserByEmailAndOauthProviderType(email, OauthProviderType.NONE);
            if (existedUser != null) {
                if (existedUser.getType() == UserType.ROLE_UNCERTIFIED_USER) {
                    userDeactivationService.deleteUncertifiedUser(existedUser.getId());
                } else if (existedUser.getEmail().contains(OauthProviderType.NONE.name())) {
                    throw new CustomException(UserErrorType.DUPLICATED_EMAIL);
                } else {
                    throw new CustomException(UserErrorType.REGISTERED_EMAIL_FOR_THE_OTHER);
                }
            }
            // 탈퇴 14일 이내 회원 확인
            if (!canRejoin(email)) {
                throw new CustomException(UserErrorType.FAIL_TO_REJOIN_14);
            }

            // 2. 비밀번호 유효성 검사
            if (Boolean.FALSE.equals(StringUtils.checkPasswordValidation(password))) {
                throw new CustomException(UserErrorType.INVALID_PASSWORD);
            }

            // 3. User&UserStatus 생성 및 저장
            User user = new User(
                    email,
                    OauthProviderType.NONE,
                    password,
                    signUpRequest.getName());
            userRepository.save(user);
            userStatusInfoService.addUserStatusInfo(user.getId());
            alarmUserService.saveDefault(user.getId());

            // 4. 포인트 관련 Entity 생성
            // (LevelPointMaster, LevelAchievement, GreenLabelPoint)
            pointService.addEntityAboutPoint(user.getId());

            // 5. UserDTO 반환
            return new UserDTO(user);
        } catch (Exception ex) {
            throw new CustomException(ex);
        }
    }

    /**
     * email로 재가입 가능 여부를 확인하는 함수
     *
     * @param email
     * @return
     */
    private boolean canRejoin(String email) {
        Optional<User> userOptional = userRepository.findByEmailLikeAndIsDeletedTrue("%_" + email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.isRejoinable();
        }

        return true;
    }

}
