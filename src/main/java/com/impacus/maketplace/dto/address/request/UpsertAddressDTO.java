package com.impacus.maketplace.dto.address.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpsertAddressDTO {

    private Long id;

    @NotBlank(message = "배송지 명칭은 필수입니다.")
    private String name;

    @NotBlank(message = "받는 분의 이름은 필수입니다.")
    private String receiver;

    @NotBlank(message = "우편 번호는 필수입니다.")
    private String postalCode;

    @NotBlank(message = "주소지는 필수입니다.")
    private String address;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String detailAddress;

    @NotBlank
    @Pattern(regexp="\\d{3}-\\d{4}-\\d{4}", message="전화번호의 형식이 유효하지 않습니다.")
    private String connectNumber;

    private String memo;
}
