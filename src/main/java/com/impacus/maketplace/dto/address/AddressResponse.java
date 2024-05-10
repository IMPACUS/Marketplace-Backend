package com.impacus.maketplace.dto.address;

import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

    private Long id;
    private String receiver;
    private String connectNumber;
    private String address;
    private String detailAddress;
    private String postalCode;
    private String memo;

    public static AddressResponse of(MyDeliveryAddress myDeliveryAddress) {
        return AddressResponse.builder()
                .id(myDeliveryAddress.getId())
                .receiver(myDeliveryAddress.getReceiver())
                .connectNumber(myDeliveryAddress.getConnectNumber())
                .address(myDeliveryAddress.getAddress())
                .detailAddress(myDeliveryAddress.getDetailAddress())
                .postalCode(myDeliveryAddress.getPostalCode())
                .memo(myDeliveryAddress.getMemo())
                .build();
    }

    public static List<AddressResponse> listOf(List<MyDeliveryAddress> myDeliveryAddresses) {
        return myDeliveryAddresses.stream()
                .map(AddressResponse::of)
                .toList();
    }
}
