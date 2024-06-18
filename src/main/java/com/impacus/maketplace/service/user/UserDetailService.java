package com.impacus.maketplace.service.user;

import com.impacus.maketplace.entity.user.UserDetail;
import com.impacus.maketplace.repository.user.UserDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailService {
    private final UserDetailRepository userDetailRepository;

    /**
     * UserDetail 저장하는 함수
     *
     * @param userId
     */
    @Transactional
    public void addUserDetail(Long userId) {
        UserDetail userDetail = new UserDetail(userId);

        userDetailRepository.save(userDetail);
    }
}
