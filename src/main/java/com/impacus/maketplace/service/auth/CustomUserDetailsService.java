package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.admin.AdminInfo;
import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.repository.admin.AdminInfoRepository;
import com.impacus.maketplace.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.CustomUserDetails;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminInfoRepository adminInfoRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 토큰 생성 시, 토큰에 추가할 사용자(소비자, 판매자, 관리자) 정보를 조회하는 함수
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // TODO 현재 flow 는 사용자 관련 entity 확정 후, 달라질 수 있음
        // 1. 소비자, 판매자에 대해서 확인
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return CustomUserDetails.toEntity(user, passwordEncoder.encode(user.getPassword()));
        }

        // 2. 관리자에 대해서 확인
        Optional<AdminInfo> optionalAdmin = adminInfoRepository.findByAdminIdName(email);
        if (optionalAdmin.isPresent()) {
            AdminInfo admin = optionalAdmin.get();
            return CustomUserDetails.toEntity(admin, passwordEncoder.encode(admin.getPassword()));
        }

        // 3. 사용자, 판매자, 관리자 관련 Entity 에서 찾을 수 없는 경우 에러 발생 시킴
        throw new CustomException(CommonErrorType.NOT_EXISTED_EMAIL);
    }

}
