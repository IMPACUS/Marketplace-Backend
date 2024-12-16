package com.impacus.maketplace.entity.consumer.oAuthToken;

import com.impacus.maketplace.common.enumType.OSType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("APPLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleOAuthToken extends OAuthToken {
    @Enumerated(EnumType.STRING)
    private OSType osType;

    public AppleOAuthToken(
            Long consumerId,
            String accessToken,
            String refreshToken,
            OSType osType
    ) {
        super(consumerId, accessToken, refreshToken);
        this.osType = osType;
    }
}
