package com.impacus.maketplace.service.auth;

import com.impacus.maketplace.common.enumType.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.entity.User;
import com.impacus.maketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_EXISTED_EMAIL));
        return CustomUserDetails.create(user);
    }

}
