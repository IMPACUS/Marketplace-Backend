package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.coupon.UserCouponStatus;
import com.impacus.maketplace.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
public class UserCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;    // PK

    @Column(nullable = false)
    private Long userId;    // 사용자 아이디

    @Column(nullable = false)
    private Long couponId;  // 쿠폰 아이디

    @Column(nullable = false)
    private LocalDateTime availableDownloadAt;   // 쿠폰을 다운로드 받을 수 있는 날짜

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean isDownload; // 쿠폰 다운로드 여부

    private LocalDateTime downloadAt;   // 쿠폰을 다운로드 받은 날짜

    @Column(nullable = false)
    @ColumnDefault("'false'")
    private Boolean isUsed; // 쿠폰 사용 여부

    private LocalDateTime usedAt;   // 쿠폰을 사용한 날짜

    private LocalDateTime expiredAt;   // 쿠폰 만료 날짜

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ValidEnum(enumClass = UserCouponStatus.class)
    private UserCouponStatus status;    // 발급된 쿠폰 상태
}
