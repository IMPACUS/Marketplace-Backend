package com.impacus.maketplace.entity.address;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Builder
public class DeliveryAddress extends Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long ordersId;

    @Builder
    public DeliveryAddress(Long ordersId, String receiver, String connectNumber, String address, String detailAddress, String postalCode, String memo, Long id) {
        super(receiver, connectNumber, address, detailAddress, postalCode, memo);
        this.ordersId = ordersId;
        this.id = id;
    }
}
