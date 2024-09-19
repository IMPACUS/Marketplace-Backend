package com.impacus.maketplace.entity.address;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
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
    private Long paymentEventId;  // 결제 이벤트 id

    @Column(nullable = false)
    private String receiver;    // 받는이

    @Column(nullable = false)
    private String postalCode;  // 우편 번호

    @Column(nullable = false)
    private String address;     // 주소지

    @Column(nullable = false)
    private String detailAddress;   // 상세 주소

    @Convert(converter = AES256ToStringConverter.class)
    private String connectNumber;   // 연락처

    private String memo;    // 메모
}
