package com.impacus.maketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import security.CustomUserDetails;

import java.util.Optional;

@Configuration
public class AuditConfig implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String userId = request.getHeader("uid");
//        return Optional.of(userId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = null;
        if (authentication != null && authentication.getPrincipal().getClass() == CustomUserDetails.class) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getId().toString();
        }

        return Optional.ofNullable(userId);
    }
}
