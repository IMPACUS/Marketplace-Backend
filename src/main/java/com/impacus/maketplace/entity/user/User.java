package com.impacus.maketplace.entity.user;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.user.UserStatus;
import com.impacus.maketplace.common.enumType.user.UserType;
import com.impacus.maketplace.common.utils.TimestampConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
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

    @ColumnDefault("'false'")
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted; // 삭제 여부

    @Column(nullable = false, unique = true)
    private String email; // Format: OauthProviderKey_Email

    @Column(nullable = false)
    private String name; // 사용자 이름

    @ColumnDefault("'ACTIVE'")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status; // 사용자 계정 상태

    private String statusReason; // 사용자 계정 상태 사유

    @ColumnDefault("'ROLE_UNCERTIFIED_USER'")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType type; // 사용자 타입

    @Column(nullable = false)
    private Boolean isAdmin; // 관리자 계정 여부

    @Column(nullable = false)
    private Boolean doesAgreeServicePolicy; // 서비스 보안 동의 여부

    @Column(nullable = false)
    private Boolean doesAgreePersonalPolicy; // 개인 정보 동의 여부

    @Column(nullable = false)
    private Boolean doesAgreeService; // 서비스 이용 여부

    @ColumnDefault("false")
    @Column(nullable = false)
    @Setter
    private Boolean firstDormancy; // 1차 휴면 상태 (6개월)

    @ColumnDefault("false")
    @Column(nullable = false)
    @Setter
    private Boolean secondDormancy; // 2차 휴면 상태 (12개월) <-

    @Column(nullable = false)
    @Setter
    private Integer dormancyMonths; // 휴면 개월 수 ( 7,8,9 개월 차와 그 이후의 포인트 감소를 구별 하기 위함)

    @Setter
    private LocalDate updateDormancyAt; // 휴면 계정 포인트 감소할 날짜

    private Long profileImageId; // 프로필 이미지 아이디

    @Convert(converter = AES256ToStringConverter.class)
    private String password; // 비밀번호

    @Convert(converter = AES256ToStringConverter.class)
    private String userJumin1; //주민 번호 앞자리

    @Convert(converter = TimestampConverter.class)
    private LocalDateTime recentLoginAt;

    @Convert(converter = AES256ToStringConverter.class)
    @ColumnDefault("'010-0000-0000'")
    @Column(nullable = false)
    private String phoneNumber; // 소비자: 휴대폰 번호/ 관리자: 판매 담당자의 수신 가능한 휴대폰 번호

    @Column(nullable = false)
    private Boolean orderDeliveryAlarm;     //  주문/배송 알람

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean restockAlarm;           //  재입고 알람

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean reviewAlarm;            //  리뷰 알람

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean serviceCenterAlarm;     //  고객센터 알람

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean brandShopAlarm;         //  브랜드샵 알람

    @ColumnDefault("true")
    @Column(nullable = false)
    private Boolean shoppingBenefitsAlarm;  //  숖핑혜택 (광고성, 쿠폰) 알람

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = UserStatus.ACTIVE;
        this.type = UserType.ROLE_UNCERTIFIED_USER;
        this.isAdmin = false;
        this.doesAgreeServicePolicy = false;
        this.doesAgreePersonalPolicy = false;
        this.doesAgreeService = false;
        this.firstDormancy = false;
        this.secondDormancy = false;
        this.dormancyMonths = 0;
        this.orderDeliveryAlarm = true;
        this.restockAlarm = true;
        this.reviewAlarm = true;
        this.serviceCenterAlarm = true;
        this.brandShopAlarm = true;
        this.shoppingBenefitsAlarm = true;
        this.phoneNumber = "010-0000-0000";
    }

    public User(String email,
                String password,
                String name,
                String phoneNumber,
                UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.status = UserStatus.ACTIVE;
        this.type = userType;
        this.isAdmin = false;
        this.doesAgreeServicePolicy = false;
        this.doesAgreePersonalPolicy = false;
        this.doesAgreeService = false;
        this.firstDormancy = false;
        this.secondDormancy = false;
        this.dormancyMonths = 0;
        this.orderDeliveryAlarm = true;
        this.restockAlarm = true;
        this.reviewAlarm = true;
        this.serviceCenterAlarm = true;
        this.brandShopAlarm = true;
        this.shoppingBenefitsAlarm = true;
    }

    public void setRecentLoginAt() {
        this.recentLoginAt = LocalDateTime.now();
    }

}
