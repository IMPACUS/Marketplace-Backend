package com.impacus.maketplace.repository.payment.checkout.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class BuyerInfoDTO {
    private Long userId;    // 사용자 id
    private String email;   // 사용자 email
    private String name;    // 사용자 이름
    private String phoneNumber; // 사용자 휴대폰 번호

    @QueryProjection
    public BuyerInfoDTO(Long userId, String email, String name, String phoneNumber) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
