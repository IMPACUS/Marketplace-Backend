package com.impacus.maketplace.dto.payment.response;

import lombok.Builder;
import lombok.Data;

@Data
public class CheckoutCustomerDTO {
    private Long customerId;  // 주문자 ID
    private String fullName;    // 전체 이름
    private String phoneNumber; // 휴대폰 번호
    private String email;   // 이메일 주소
    private Address address;    // 주소
    private String zipcode;     // 우편번호

    public CheckoutCustomerDTO(Long customerId, String fullName, String phoneNumber, String email, String address, String detailAddress, String postalCode) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.zipcode = postalCode;
        this.address = Address.builder()
                .country("KR")
                .addressLine1(address)
                .addressLine2(detailAddress)
                .build();
    }

    @Data
    @Builder
    private static class Address {
        private String country = "KR";  // 국가
        private String addressLine1;    // 일반 주소
        private String addressLine2;    // 상세 주소
    }
}
