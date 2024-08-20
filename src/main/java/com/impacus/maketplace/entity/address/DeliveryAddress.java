package com.impacus.maketplace.entity.address;

import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DeliveryAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_id")
    private Long id;

    @Column(nullable = false)
    private Long ordersId;  // 주문 아이디

    @Column(nullable = false)
    private String receiver;    // 받는이

    @Column(nullable = false)
    private String postalCode;  // 우편 번호

    @Column(nullable = false)
    private String address;     // 주소지

    @Column(nullable = false)
    private String detailAddress;   // 상세 주소

    private String connectNumber;   // 연락처

    private String memo;    // 메모
}
