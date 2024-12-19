package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.constants.DaysConstants;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.TimestampConverter;
import com.impacus.maketplace.dto.user.PhoneNumberDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String email; // Format: OauthProviderKey_Email

    @Convert(converter = AES256ToStringConverter.class)
    private String password; // 비밀번호

    @Column(nullable = false)
    private String name;

    @Comment("회원 아이디")
    private String userIdName;

    @ColumnDefault("'ROLE_UNCERTIFIED_USER'")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type; // 사용자 타입

    @Convert(converter = AES256ToStringConverter.class)
    @Comment("주민 번호 앞자리")
    private String jumin1;

    @Convert(converter = AES256ToStringConverter.class)
    @Column(nullable = false)
    private String phoneNumberPrefix;

    @Column(nullable = false)
    private String phoneNumberSuffix;

    private Long profileImageId; // 프로필 이미지 아이디

    @Convert(converter = TimestampConverter.class)
    private LocalDateTime recentLoginAt;

    @Comment("이메일 동의 여부")
    @ColumnDefault("true")
    @Column(nullable = false, name = "is_cert_email")
    private boolean isCertEmail;

    @Comment("휴대전화 동의 여부")
    @ColumnDefault("true")
    @Column(nullable = false, name = "is_cert_phone")
    private boolean isCertPhone;

    @Comment("이메일 동의 날짜")
    @ColumnDefault("NOW()")
    @Column(nullable = false)
    private LocalDateTime certEmailAt;

    @Comment("휴대전화 동의 날짜")
    @ColumnDefault("NOW()")
    @Column(nullable = false)
    private LocalDateTime certPhoneAt;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    public boolean isRejoinable() {
        if (this.isDeleted &&
                this.getModifyAt().plusDays(DaysConstants.REJOIN_RESTRICTION_DATE).isAfter(LocalDateTime.now())
        ) {
            return false;
        }
        return true;
    }

    public User(String email, String password, String name) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = UserType.ROLE_UNCERTIFIED_USER;
        this.isCertEmail = true;
        this.isCertPhone = false;
        this.certEmailAt = LocalDateTime.now();
        this.certPhoneAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    public User(String email,
                String password,
                String name,
                String phoneNumber,
                UserType userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = userType;
        this.isCertEmail = true;
        this.isCertPhone = true;
        this.certEmailAt = LocalDateTime.now();
        this.certPhoneAt = LocalDateTime.now();
        this.isDeleted = false;

        setPhoneNumber(phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumberPrefix + "-" + phoneNumberSuffix;
    }

    private void setPhoneNumber(String phoneNumber) {
        PhoneNumberDTO dto = new PhoneNumberDTO(phoneNumber);
        this.phoneNumberPrefix = dto.getPhoneNumberPrefix();
        this.phoneNumberSuffix = dto.getPhoneNumberSuffix();
    }

    public void setRecentLoginAt() {
        this.recentLoginAt = LocalDateTime.now();
    }

}
