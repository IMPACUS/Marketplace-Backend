package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import com.impacus.maketplace.common.utils.TimeUtils;
import com.impacus.maketplace.entity.seller.Brand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class ChangeBrandInfoDTO {
    @NotBlank
    private String brandName;

    @NotBlank
    @ValidPhoneNumber
    private String customerServiceNumber; // 고객센터 전화번호

    @Email
    @NotBlank
    private String representativeName; // 대표 이메일

    @NotBlank
    private String brandIntroduction; // 쇼핑몰 소개

    @NotNull
    private LocalTime openingTime; // HH:mm:ss, HH:mm

    @NotNull
    private LocalTime closingTime;

    @NotBlank
    private String businessDay;

    @NotBlank
    private String breakingTime;

    public Brand toEntity(Long sellerId) {
        return Brand.builder()
                .sellerId(sellerId)
                .introduction(brandIntroduction)
                .openingTime(openingTime)
                .closingTime(closingTime)
                .businessDay(businessDay)
                .breakingTime(breakingTime)
                .build();
    }

    public void roundOpeningTime() {
        this.openingTime = TimeUtils.roundTimeToPolicy(this.openingTime);
    }
}
