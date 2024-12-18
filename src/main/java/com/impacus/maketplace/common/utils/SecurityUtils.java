package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.user.UserType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import security.CustomUserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public final class SecurityUtils {

    /**
     * 현재 API 요청에서 사용된 요청자의 권한을 반환하는 함수
     * - 요청한 API 의 권한이 없는 경우, ROLE_NONE
     *
     * @return
     */
    public static UserType getCurrentUserType() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            String roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

            return UserType.fromName(roles);
        } catch (Exception ex) {
            return UserType.ROLE_NONE;
        }
    }

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return null;
        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }
    
    public static UserType getCurrentUserFromCustomUserDetails(CustomUserDetails customUserDetail) {
        try {
            String roles = customUserDetail.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));
            return UserType.fromName(roles);
        } catch (Exception e) {
            return UserType.ROLE_NONE;
        }
    }
}
