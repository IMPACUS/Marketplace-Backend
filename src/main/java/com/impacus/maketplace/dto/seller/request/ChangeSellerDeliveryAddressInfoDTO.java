package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidAccountNumber;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.entity.seller.delivery.SellerDeliveryAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class ChangeSellerDeliveryAddressInfoDTO {
    // 배송 주소 ID
    private Long deliveryAddressId;

    // 일반 주소지
    @NotBlank(message = "일반 주소지는 필수입니다")
    @Size(max = 255, message = "일반 주소지는 최대 255자까지 입력 가능합니다")
    private String generalAddress;

    // 일반 상세 주소
    @NotBlank(message = "일반 상세 주소는 필수입니다")
    @Size(max = 255, message = "일반 상세 주소는 최대 255자까지 입력 가능합니다")
    private String generalDetailAddress;

    // 일반 상호명
    @NotBlank(message = "일반 상호명은 필수입니다")
    @Size(max = 25)
    private String generalBusinessName;

    // 반품 주소지
    @NotBlank(message = "반품 주소지는 필수입니다")
    private String refundAddress;

    // 반품 상세 주소
    @NotBlank(message = "반품 상세 주소는 필수입니다")
    private String refundDetailAddress;

    // 반품 상호명
    @NotBlank(message = "반품 상호명은 필수입니다")
    @Size(max = 25)
    private String refundBusinessName;

    // 반품 배송비 계좌번호
    @NotBlank(message = "반품 배송비 계좌번호는 필수입니다")
    @Size(max = 25)
    private String refundAccountNumber;

    // 반품 배송비 예금주명
    @NotBlank(message = "반품 배송비 예금주명은 필수입니다")
    @ValidAccountNumber
    @Size(max = 25)
    private String refundAccountName;

    // 반품 은행 코드
    @NotBlank(message = "반품 은행 코드는 필수입니다")
    @ValidEnum(enumClass = BankCode.class)
    private BankCode refundBankCode;

    public SellerDeliveryAddress toEntity(Long sellerId) {
        return SellerDeliveryAddress.builder()
                .sellerId(sellerId)
                .generalAddress(this.generalAddress)
                .generalDetailAddress(this.generalDetailAddress)
                .generalBusinessName(this.generalBusinessName)
                .refundAddress(refundAddress)
                .refundDetailAddress(refundDetailAddress)
                .refundBusinessName(refundBusinessName)
                .refundAccountNumber(refundAccountNumber)
                .refundAccountName(refundAccountName)
                .refundBankCode(this.refundBankCode)
                .build();
    }
}
