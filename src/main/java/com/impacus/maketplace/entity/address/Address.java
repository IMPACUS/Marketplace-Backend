package com.impacus.maketplace.entity.address;


import com.impacus.maketplace.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Address extends BaseEntity {

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "connect_number")
    private String connectNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "memo")
    private String memo;

}
