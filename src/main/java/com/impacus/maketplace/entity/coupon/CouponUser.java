package com.impacus.maketplace.entity.coupon;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CouponUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean couponLock; // 쿠폰을 다운로드 받을 수 없는 상태 (N일 뒤 발급)

    private LocalDateTime availableDownloadAt;  //  N일뒤 발급시에 대한 기간

    @Builder.Default
    private Boolean isDownloaded = false;

    @Builder.Default
    private Boolean isUsed = false;

    @Builder.Default
    private LocalDateTime usedAt = null;

    private LocalDateTime expiredAt;

}
