package com.impacus.maketplace.entity.consumer;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "oauth_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_token_id")
    private Long id;

    @Comment("user_info 의 FK")
    @Column(nullable = false, unique = true)
    private Long consumerId;

    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;
}
