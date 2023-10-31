package com.impacus.maketplace.entity;

import com.impacus.maketplace.common.BaseTimeEntity;
import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.common.enumType.UserHistoryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPrivacy extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_history_id")
    private Long id;

    @Column(nullable = false)
    private Long userId; // 사용자

    private Long profileImageId; // 프로필 이미지 아이디

    private String password; // 비밀번호

    private int wrongPasswordCnt; // 비밀번호 틀린 횟수

    @Enumerated(EnumType.ORDINAL)
    private BankCode bankCode; // 은행 코드

    private String bankAccountNumber; // 은행 계좌 번호

    private String userJumin1; //주민 번호 앞자리

    private String userJumin2; //주민 번호 뒷자리

    private String authCi;

    private String authDi;

    private String pccc; // 개인 통관 고유 번호


}
