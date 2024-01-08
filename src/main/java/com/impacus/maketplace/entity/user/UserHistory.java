package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.enumType.user.UserHistoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_history_id")
    private Long id;

    @Column(nullable = false)
    private Long userId; // 사용자

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserHistoryType historyType; // 이력 타입

    private String description; // 이력 설명
}
