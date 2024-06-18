package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "user_detail")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_detail_id")
    private Long id;

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Column(nullable = false, unique = true)
    private Long userId; // user_info의 FK

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isCertEmail; // 이메일 인증 여부

    private LocalDateTime certEmailDateTime; // 이메일 인증 시간

    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isCertPhone; // 핸드폰 인증 여부

    private LocalDateTime certPhoneDateTime; // 핸드폰 인증 시간

    @ColumnDefault("'NONE'")
    @Enumerated(EnumType.STRING)
    private PaymentMethod selectedPaymentMethod; // 선택된 결제 수단

    @Convert(converter = AES256ToStringConverter.class)
    private String userJumin2; //주민 번호 뒷자리

    @Convert(converter = AES256ToStringConverter.class)
    private String pccc; // 개인 통관 고유 번호

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long greenLabelPoint; // 그린 라벨 포인트

    public UserDetail(Long userId) {
        this.isDeleted = false;
        this.userId = userId;
        this.isCertEmail = false;
        this.isCertPhone = false;
        this.greenLabelPoint = 0L;
    }
}
