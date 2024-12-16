package com.impacus.maketplace.entity.consumer.oAuthToken;

import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "oauth_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class OAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_token_id")
    private Long id;

    @Comment("user_info 의 FK")
    @Column(nullable = false, unique = true)
    private Long consumerId;

    @Convert(converter = AES256ToStringConverter.class)
    private String accessToken;

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String refreshToken;

    public OAuthToken(
            Long consumerId,
            String accessToken,
            String refreshToken
    ) {
        this.consumerId = consumerId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
