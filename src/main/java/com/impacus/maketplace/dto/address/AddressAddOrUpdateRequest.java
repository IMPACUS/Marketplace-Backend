package com.impacus.maketplace.dto.address;

import com.impacus.maketplace.entity.address.MyDeliveryAddress;
import com.impacus.maketplace.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressAddOrUpdateRequest {

    @NotBlank
    private String receiver;

    @NotBlank
    @Pattern(regexp="\\d{3}-\\d{4}-\\d{4}", message="전화번호의 형식이 유효하지 않습니다.")
    private String connectNumber;

    @NotBlank
    private String address;

    @NotBlank
    private String detailAddress;

    @NotBlank
    private String postalCode;

    private String memo;

    public MyDeliveryAddress.MyDeliveryAddressBuilder toEntity() {
        return MyDeliveryAddress.builder()
                .receiver(receiver)
                .connectNumber(connectNumber)
                .address(address)
                .detailAddress(detailAddress)
                .postalCode(postalCode)
                .memo(memo);
    }

    public MyDeliveryAddress toEntity(User user) {
        return toEntity()
                .user(user)
                .build();
    }

}