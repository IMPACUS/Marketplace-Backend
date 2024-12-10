package com.impacus.maketplace.repository.consumer;

import com.impacus.maketplace.entity.consumer.OAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthTokenRepository extends JpaRepository<OAuthToken, Long> {
    Optional<OAuthToken> findByConsumerId(Long consumerId);

    @Modifying
    @Query("UPDATE OAuthToken o " +
            "SET o.accessToken = :accessToken, o.refreshToken = :refreshToken " +
            "WHERE o.id = :id"
    )
    void updateOAuthToken(@Param("id") Long id,
                          @Param("accessToken") String accessToken,
                          @Param("refreshToken") String refreshToken
    );
}
