package com.impacus.maketplace.entity;

import com.impacus.maketplace.common.BaseTimeEntity;
import com.impacus.maketplace.common.enumType.UserHistoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_history_id")
    private Long id;

    @Column(nullable = false)
    private Long userId; // 사용자

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserHistoryType historyType; // 이력 타입

    private String description; // 이력 설명
}
