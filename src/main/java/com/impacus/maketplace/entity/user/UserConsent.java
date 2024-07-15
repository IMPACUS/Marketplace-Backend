package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "user_consent")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserConsent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_consent_id")
    private Long id;

    @Comment("user_info의 FK")
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    @Comment("이용 약관 동의 여부")
    private Boolean doesAgreeServicePolicy; //

    @Column(nullable = false)
    @Comment("개인 정보 동의 여부")
    private Boolean doesAgreePersonalPolicy;

    @Column(nullable = false)
    @Comment("이용 약관 동의 날짜")
    private LocalDateTime servicePolicyConsentAt;

    @Column(nullable = false)
    @Comment("개인 정보 동의 날짜")
    private LocalDateTime personalPolicyConsentAt;
}
