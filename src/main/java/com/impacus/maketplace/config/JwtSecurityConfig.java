package com.impacus.maketplace.config;

import com.impacus.maketplace.config.filter.JwtFilter;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import com.impacus.maketplace.redis.service.BlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenProvider tokenProvider;
    private final BlacklistService blacklistService;

    @Override
    public void configure(HttpSecurity http) {

        http.addFilterBefore(
                new JwtFilter(tokenProvider, blacklistService),
            UsernamePasswordAuthenticationFilter.class
        );
    }

}
