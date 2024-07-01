package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder
@Table(name = "user_status_info")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatusInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_status_info_id")
    private Long id;

    @Comment("user_info의 FK")
    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Comment("사용자 계정 상태 이유")
    private String statusReason;

    public UserStatusInfo(Long userId) {
        this.userId = userId;
        this.status = UserStatus.ACTIVE;
        this.statusReason = "사용자 신규 생성";
    }

    public static UserStatusInfo toEntity(Long userId) {
        return new UserStatusInfo(userId);
    }
}
