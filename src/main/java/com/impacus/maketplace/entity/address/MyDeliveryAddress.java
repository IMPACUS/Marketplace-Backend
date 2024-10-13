package com.impacus.maketplace.entity.address;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MyDeliveryAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_delivery_address_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;    // 사용자 아이디

    @Column(nullable = false)
    @ColumnDefault("'배송지1'")
    private String name;    // 배송지 이름

    @Column(nullable = false)
    private String receiver;    // 받는이

    @Column(nullable = false)
    private String postalCode;  // 우편 번호

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String detailAddress;   // 메모

    @Convert(converter = AES256ToStringConverter.class)
    private String connectNumber;   // 연락처

    private String memo;    // 메모

    public void updateAddress(String receiver, String postalCode, String address, String detailAddress, String connectNumber, String memo) {
        this.receiver = receiver;
        this.postalCode = postalCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.connectNumber = connectNumber;
        this.memo = memo;
    }
}
