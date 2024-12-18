package com.impacus.maketplace.entity.consumer.oAuthToken;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("COMMON")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonOAuthToken extends OAuthToken {

    public CommonOAuthToken(
            Long consumerId,
            String accessToken,
            String refreshToken
    ) {
        super(consumerId, accessToken, refreshToken);
    }
}
