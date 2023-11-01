package com.impacus.maketplace.entity;

import com.impacus.maketplace.common.BaseTimeEntity;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.enumType.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; // Format: OauthProviderKey_Email

    @Column(nullable = false)
    private String name; // 사용자 이름

    @ColumnDefault("1")
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserStatus status; // 사용자 계정 상태

    private String statusReason; // 사용자 계정 상태 사유

    private String type; // ..?

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long greenLablePoint; // 그린 라벨 포인트

    @Column(nullable = false)
    private Boolean isAdmin; // 관리자 계정 여부

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isCertEmail; // 이메일 인증 여부

    private LocalDateTime certEmailDateTime; // 이메일 인증 시간

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isCertPhone; // 핸드폰 인증 여부

    private LocalDateTime certPhoneDateTime; // 핸드폰 인증 시간

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isCertBank; // 계좌 인증 여부

    private LocalDateTime certBankDateTime; // 계좌 인증 시간

    @Column(nullable = false)
    private Boolean doesAgreeServicePolicy; // 서비스 보안 동의 여부

    @Column(nullable = false)
    private Boolean doesAgreePersonalPolicy; // 개인 정보 동의 여부

    @Column(nullable = false)
    private Boolean doesAgreeService; // 서비스 이용 여부

    @Column(nullable = false)
    private Boolean isWithdrawn; // 철회 여부

    @Column(nullable = false)
    private Boolean withdrawnDateTime; // 철회 진행 시간

    @Column(nullable = false)
    private Boolean isDormancy; // 휴면 계정 여부

    @Column(nullable = false)
    private Boolean dormancyDateTime; // 철회 진행 시간

    @ColumnDefault("1")
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod selectedPaymentMethod; // 선택된 결제 수단

}
