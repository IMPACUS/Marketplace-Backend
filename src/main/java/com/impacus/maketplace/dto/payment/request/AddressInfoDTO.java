package com.impacus.maketplace.dto.payment.request;

import com.impacus.maketplace.entity.address.DeliveryAddress;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressInfoDTO {
    private String name;
    private String receiver;
    private String postalCode;
    private String address;
    private String detailAddress;
    private String connectNumber;
    private String memo;

    public DeliveryAddress toEntity(Long paymentEventId) {
        return DeliveryAddress.builder()
                .paymentEventId(paymentEventId)
                .receiver(this.receiver)
                .postalCode(this.postalCode)
                .address(this.address)
                .detailAddress(this.detailAddress)
                .connectNumber(this.connectNumber)
                .memo(this.memo)
                .build();
    }
}
