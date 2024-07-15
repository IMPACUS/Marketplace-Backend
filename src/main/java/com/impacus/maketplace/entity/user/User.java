package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.TimestampConverter;
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

    @Column(nullable = false, unique = true)
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
    @Comment("주민 번호 뒷자리")
    private String jumin2;

    @Convert(converter = AES256ToStringConverter.class)
    @ColumnDefault("'010-0000-0000'")
    @Column(nullable = false)
    private String phoneNumber;

    private Long profileImageId; // 프로필 이미지 아이디

    @Convert(converter = TimestampConverter.class)
    private LocalDateTime recentLoginAt;

    @Comment("이메일 동의 여부")
    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean isCertEmail;

    @Comment("휴대전화 동의 여부")
    @ColumnDefault("true")
    @Column(nullable = false)
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

    public User(String email, String password, String name) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = UserType.ROLE_CERTIFIED_USER;
        this.phoneNumber = "010-0000-0000";
    }

    public User(String email,
                String password,
                String name,
                String phoneNumber,
                UserType userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.type = userType;
    }

    public void setRecentLoginAt() {
        this.recentLoginAt = LocalDateTime.now();
    }

}
