package com.impacus.maketplace.entity.consumer.oAuthToken;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("KAKAO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoOAuthToken extends OAuthToken {

    @Column(name = "oauth_user_id")
    private Long oAuthUserId;

    public KakaoOAuthToken(
            Long consumerId,
            String accessToken,
            String refreshToken,
            Long oAuthUserId
    ) {
        super(consumerId, accessToken, refreshToken);
        this.oAuthUserId = oAuthUserId;
    }
}
