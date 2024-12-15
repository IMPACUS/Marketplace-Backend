package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.common.enumType.error.UserErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.entity.consumer.OAuthToken;
import com.impacus.maketplace.repository.consumer.ConsumerRepository;
import com.impacus.maketplace.repository.consumer.OAuthTokenRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommonOAuthService {
    private final ConsumerRepository consumerRepository;
    private final OAuthTokenRepository oAuthTokenRepository;

    public void saveOrUpdateOAuthToken(Long userId, OAuthTokenDTO oauthTokenDTO) {
        saveOrUpdateOAuthToken(userId, oauthTokenDTO, null);
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
            @Nullable LocalDate refreshTokenExpiresIn
    ) {
        Optional<Long> consumerId = consumerRepository.findIdByUserId(userId);
        if (consumerId.isPresent()) {
            Optional<OAuthToken> optionalOAuthToken = oAuthTokenRepository.findByConsumerId(consumerId.get());
            if (optionalOAuthToken.isPresent()) {
                updateOAuthToken(
                        optionalOAuthToken.get().getId(),
                        oauthTokenDTO,
                        refreshTokenExpiresIn
                );
            } else {
                OAuthToken oAuthToken = oauthTokenDTO.toEntity(consumerId.get(), refreshTokenExpiresIn);
                oAuthTokenRepository.save(oAuthToken);
            }
        }
    }

    @Transactional
    public void updateOAuthToken(Long oAuthTokenId, OAuthTokenDTO oauthTokenDTO) {
        updateOAuthToken(oAuthTokenId, oauthTokenDTO, null);
    }

    @Transactional
    public void updateOAuthToken(
            Long oAuthTokenId,
            OAuthTokenDTO oauthTokenDTO,
            @Nullable LocalDate refreshExpiredAt
    ) {
        if (refreshExpiredAt != null) {
            oAuthTokenRepository.updateOAuthToken(
                    oAuthTokenId,
                    oauthTokenDTO.getAccessToken(),
                    oauthTokenDTO.getRefreshToken(),
                    refreshExpiredAt
            );
        } else {
            oAuthTokenRepository.updateOAuthToken(
                    oAuthTokenId,
                    oauthTokenDTO.getAccessToken(),
                    oauthTokenDTO.getRefreshToken()
            );
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
