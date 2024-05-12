package com.impacus.maketplace.entity.address;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "delivery_address")
@NoArgsConstructor
public class DeliveryAddress extends Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder
    public DeliveryAddress(String receiver, String connectNumber, String address, String detailAddress, String postalCode, String memo, Long id) {
        super(receiver, connectNumber, address, detailAddress, postalCode, memo);
        this.id = id;
    }
}
