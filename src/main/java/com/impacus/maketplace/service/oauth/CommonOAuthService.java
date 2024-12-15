package com.impacus.maketplace.service.oauth;

import com.impacus.maketplace.dto.oauth.request.OAuthTokenDTO;
import com.impacus.maketplace.entity.consumer.OAuthToken;
import com.impacus.maketplace.repository.consumer.ConsumerRepository;
import com.impacus.maketplace.repository.consumer.OAuthTokenRepository;
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
    public void saveOrUpdateOAuthToken(Long userId, OAuthTokenDTO oauthTokenDTO, LocalDate refreshTokenExpiresIn) {
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
                OAuthToken oAuthToken = oauthTokenDTO.toEntity(consumerId.get(), refreshTokenExpiresIn);
                oAuthTokenRepository.save(oAuthToken);
            }
        }
    }
}
