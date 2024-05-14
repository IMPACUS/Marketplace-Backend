package com.impacus.maketplace.entity.address;


import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.dto.address.AddressAddOrUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
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

    public void update(AddressAddOrUpdateRequest request) {
        this.receiver = request.getReceiver();
        this.connectNumber = request.getConnectNumber();
        this.address = request.getAddress();
        this.detailAddress = request.getDetailAddress();
        this.postalCode = request.getPostalCode();
        this.memo = request.getMemo();
    }

}
