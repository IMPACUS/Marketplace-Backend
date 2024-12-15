package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.common.enumType.OSType;
import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.entity.consumer.oAuthToken.OAuthToken;
import com.impacus.maketplace.repository.consumer.ConsumerRepository;
import com.impacus.maketplace.repository.consumer.OAuthTokenRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommonOAuthService {
    private final ConsumerRepository consumerRepository;
    private final OAuthTokenRepository oAuthTokenRepository;

    public void saveOrUpdateOAuthToken(Long userId, OAuthTokenDTO oauthTokenDTO) {
        saveOrUpdateOAuthToken(userId, oauthTokenDTO, null, null);
    }

    public void saveOrUpdateOAuthToken(Long userId, OAuthTokenDTO oauthTokenDTO, Long oAuthUserId) {
        saveOrUpdateOAuthToken(userId, oauthTokenDTO, oAuthUserId, null);
    }

    public void saveOrUpdateOAuthToken(Long userId, OAuthTokenDTO oauthTokenDTO, OSType osType) {
        saveOrUpdateOAuthToken(userId, oauthTokenDTO, null, osType);
    }

    /**
     * OAuthToken 저장 함수
     *
     * @param userId
     * @param oauthTokenDTO
     */
    @Transactional
    public void saveOrUpdateOAuthToken(
            Long userId,
            OAuthTokenDTO oauthTokenDTO,
            @Nullable Long oAuthUserId,
            @Nullable OSType osType
    ) {
        Optional<Long> consumerId = consumerRepository.findIdByUserId(userId);
        if (consumerId.isPresent()) {
            Optional<OAuthToken> optionalOAuthToken = oAuthTokenRepository.findByConsumerId(consumerId.get());
            if (optionalOAuthToken.isPresent()) {
                oAuthTokenRepository.updateOAuthToken(
                        optionalOAuthToken.get().getId(),
                        oauthTokenDTO.getAccessToken(),
                        oauthTokenDTO.getRefreshToken()
                );
            } else {
                OAuthToken oAuthToken = createOAuthToken(consumerId.get(), oauthTokenDTO, oAuthUserId, osType);
                oAuthTokenRepository.save(oAuthToken);
            }
        }
    }

    public OAuthToken createOAuthToken(
            Long consumerId,
            OAuthTokenDTO oauthTokenDTO,
            @Nullable Long oAuthUserId,
            @Nullable OSType osType
    ) {
        if (oAuthUserId != null) {
            return oauthTokenDTO.toEntity(consumerId, oAuthUserId);
        } else if (osType != null) {
            return oauthTokenDTO.toEntity(consumerId, osType);
        } else {
            return oauthTokenDTO.toEntity(consumerId);
        }
    }

    public OAuthToken findOAuthTokenByUserId(Long userId) {
        Optional<Long> consumerId = consumerRepository.findIdByUserId(userId);
        if (consumerId.isPresent()) {
            return oAuthTokenRepository.findByConsumerId(consumerId.get())
                    .orElseThrow(() -> new CustomException(UserErrorType.NOT_EXISTED_OAUTH_TOKEN));
        } else {
            throw new CustomException(UserErrorType.NOT_EXISTED_CONSUMER);
        }
    }
}
