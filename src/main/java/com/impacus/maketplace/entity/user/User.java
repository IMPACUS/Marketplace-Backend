package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@Table(name = "user_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

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

    @ColumnDefault("1")
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserType type; // 사용자 타입

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long greenLabelPoint; // 그린 라벨 포인트

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

    private LocalDateTime withdrawnDateTime; // 철회 진행 시간

    @Column(nullable = false)
    private Boolean isDormancy; // 휴면 계정 여부

    private LocalDateTime dormancyDateTime; // 철회 진행 시간

    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod selectedPaymentMethod; // 선택된 결제 수단

    private Long profileImageId; // 프로필 이미지 아이디

    @Convert(converter = AES256ToStringConverter.class)
    private String password; // 비밀번호

    private int wrongPasswordCnt; // 비밀번호 틀린 횟수

    @Enumerated(EnumType.ORDINAL)
    private BankCode bankCode; // 은행 코드

    @Convert(converter = AES256ToStringConverter.class)
    private String bankAccountNumber; // 은행 계좌 번호

    @Convert(converter = AES256ToStringConverter.class)
    private String userJumin1; //주민 번호 앞자리

    @Convert(converter = AES256ToStringConverter.class)
    private String userJumin2; //주민 번호 뒷자리

    @Convert(converter = AES256ToStringConverter.class)
    private String authCi;

    @Convert(converter = AES256ToStringConverter.class)
    private String authDi;

    @Convert(converter = AES256ToStringConverter.class)
    private String pccc; // 개인 통관 고유 번호

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = UserStatus.ACTIVE;
        this.type = UserType.ROLE_UNCERTIFIED_USER;
        this.greenLabelPoint = 0L;
        this.isAdmin = false;
        this.isCertEmail = false;
        this.isCertPhone = false;
        this.isCertBank = false;
        this.doesAgreeServicePolicy = false;
        this.doesAgreePersonalPolicy = false;
        this.doesAgreeService = false;
        this.isWithdrawn = false;
        this.isDormancy = false;
    }

}
