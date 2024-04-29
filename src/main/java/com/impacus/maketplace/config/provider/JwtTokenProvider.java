package com.impacus.maketplace.config.provider;


import com.impacus.maketplace.common.enumType.error.CommonErrorType;
import com.impacus.maketplace.common.enumType.error.TokenErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import security.CustomUserDetails;

import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private static final String GRANT_TYPE = "Bearer";
    private final String jwtSecretKey;
    private final Long accessTokenValidityInMin;
    private final Long refreshTokenValidityInMin;
    private String jwtKey;

    public JwtTokenProvider(
            @Value("${key.jwt.secret-key}") String jwtSecretKey,
            @Value("${key.jwt.accesstoken-validity-in-min}") Long accessTokenValidityInMin,
            @Value("${key.jwt.refreshtoken-validity-in-min}") Long refreshTokenValidityInMin) {
        this.jwtSecretKey = jwtSecretKey;
        this.accessTokenValidityInMin = accessTokenValidityInMin * 1000L * 60;
        this.refreshTokenValidityInMin = refreshTokenValidityInMin * 1000L * 60;
    }

    @Override
    public void afterPropertiesSet() {
        String jwtOriginSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        this.jwtKey = jwtOriginSecretKey + jwtOriginSecretKey.substring(0, 30);
    }

    /**
     * JWT token 정보를 생성하는 함수
     *
     * @return access token과 refresh token이 담긴 jwt 토큰 객체
     */
    public TokenInfoVO createToken(Authentication authentication) {
        // 1. 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 2. userID 가져오기
        Long userId = ((CustomUserDetails) authentication.getPrincipal()).getId();

        // 3. Access token 생성
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenValidityInMin);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("role", authorities)
                .claim("id", userId.toString())
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();

        // 4. Refresh token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + refreshTokenValidityInMin))
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();

        return TokenInfoVO.toVO(GRANT_TYPE, accessToken, refreshToken);
    }

    /**
     * JWT access token을 복호화하여 토큰에 들어있는 정보를 꺼내는 함수
     *
     * @param accessToken
     * @return
     */
    public Authentication getAuthentication(String accessToken) {
        // 1. 토큰 복호화
        Claims claims = parseClaims(accessToken);

        // 2. 토큰 권한 확인
        if (claims.get("role") == null) {
            throw new CustomException(HttpStatus.NOT_FOUND, CommonErrorType.INVALID_TOKEN,
                    "권한 정보가 없는 토큰입니다.");
        }

        // 3. Claim 에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        // 4. user id 가져오기
        Long userId = Long.parseLong((String) claims.get("id"));

        // 5. CustomUserDetails 생성
        CustomUserDetails principal = CustomUserDetails.builder()
                .id(userId)
                .email(claims.getSubject())
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * JWT access token 만료되었을 때, 갱신을 하기 위해 필요한 정보를 얻기 위한 Claim 반환하는 함수
     *
     * @param accessToken
     * @return
     */
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * token의 유효성을 확인하는 함수
     *
     * @param jwtToken
     * @return
     */
    public TokenErrorType validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtKey).build().parseClaimsJws(jwtToken);
            return TokenErrorType.NONE;
        } catch (ExpiredJwtException e) {
            return TokenErrorType.EXPIRED_TOKEN;
        } catch (UnsupportedJwtException e) {
            return TokenErrorType.UNSUPPORTED_TOKEN;
        } catch (IllegalStateException | SignatureException e) {
            return TokenErrorType.INVALID_TOKEN;
        }
    }
}
