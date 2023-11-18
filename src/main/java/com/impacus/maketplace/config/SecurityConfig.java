package com.impacus.maketplace.config;

import com.impacus.maketplace.common.handler.JwtAccessDeniedHandler;
import com.impacus.maketplace.config.endpoint.JwtAuthenticationEntryPoint;
import com.impacus.maketplace.config.provider.JwtTokenProvider;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private static final String[] DEFAULT_WHITELIST = {
        "/status", "/images/**", "/error/**,/favicon.ico"
    };
    private static final String[] AUTH_WHITELIST = {
        "/auth/sign-up/**", "/auth/login/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler);
        http.cors();
        http.authorizeHttpRequests(request -> request
            .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
            .requestMatchers(DEFAULT_WHITELIST).permitAll()
            .requestMatchers(AUTH_WHITELIST).permitAll()
            .anyRequest().authenticated()
        );
        http.apply(new JwtSecurityConfig(jwtTokenProvider));
        return http.build();
    }
}
