package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ChangeBrandInfoDTO {
    @NotBlank
    private String brandName;

    @NotBlank
    @ValidPhoneNumber
    private String contactNumber; // 고객센터 전화번호

    @Email
    @NotBlank
    private String representativeName; // 대표 이메일

    @NotBlank
    private String brandIntroduction; // 쇼핑몰 소개

    @NotBlank
    private LocalTime openingTime; // HH:mm:ss, HH:mm

    @NotBlank
    private LocalTime closingTime;

    @NotBlank
    private String businessDay;

    @NotBlank
    private String breakingTime;
}
