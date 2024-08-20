package com.impacus.maketplace.dto.address.response;

import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import lombok.Data;

@Data
public class AddressInfoDTO {
    private Long id;
    private String name;
    private String receiver;
    private String postalCode;
    private String address;
    private String detailAddress;
    private String connectNumber;
    private String memo;

    public AddressInfoDTO(MyDeliveryAddress myDeliveryAddress) {
        this.id = myDeliveryAddress.getId();
        this.name = myDeliveryAddress.getName();
        this.receiver = myDeliveryAddress.getReceiver();
        this.postalCode = myDeliveryAddress.getPostalCode();
        this.address = myDeliveryAddress.getAddress();
        this.detailAddress = myDeliveryAddress.getDetailAddress();
        this.connectNumber = myDeliveryAddress.getConnectNumber();
        this.memo = myDeliveryAddress.getMemo();
    }
}
