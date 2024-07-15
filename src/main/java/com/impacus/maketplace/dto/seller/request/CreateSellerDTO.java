package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidAccountNumber;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.BankCode;
import com.impacus.maketplace.common.enumType.seller.BusinessType;
import com.impacus.maketplace.entity.seller.Seller;
import com.impacus.maketplace.entity.seller.SellerAdjustmentInfo;
import com.impacus.maketplace.entity.seller.SellerBusinessInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSellerDTO {
    @NotBlank(message = "이메일은 공백이 될 수 없습니다.")
    @Email(message = "올바른 형식의 이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 공백이 될 수 없습니다.")
    private String password;

    @Size(max = 25)
    @NotBlank
    private String contactName;

    @Size(max = 25)
    @NotBlank
    private String contactNumber;

    @Size(max = 25)
    @NotBlank
    private String marketName;

    @Size(max = 25)
    @NotBlank
    private String customerServiceNumber;

    @ValidEnum(enumClass = BusinessType.class)
    private BusinessType businessType;

    @Size(max = 25)
    @NotBlank
    private String representativeName;

    @Size(max = 25)
    @NotBlank
    private String representativeContact;

    @Size(max = 25)
    @NotBlank
    private String businessName;

    @Size(max = 25)
    @NotBlank
    private String businessRegistrationNumber;

    @Size(max = 25)
    @NotBlank
    private String businessCondition;

    @Size(max = 25)
    @NotBlank
    private String businessAddress;

    @Size(max = 25)
    @NotBlank
    private String businessEmail;

    @Size(max = 25)
    private String mailOrderBusinessReportNumber;

    @ValidEnum(enumClass = BankCode.class)
    private BankCode bankCode;

    @Size(max = 25)
    @NotBlank
    private String accountName;

    @Size(max = 25)
    @NotBlank
    @ValidAccountNumber
    private String accountNumber;


    public Seller toSellerEntity(Long userId, Long logoImageId) {
        return Seller.builder()
                .userId(userId)
                .contactName(this.contactName)
                .marketName(this.marketName)
                .logoImageId(logoImageId)
                .businessType(this.businessType)
                .customerServiceNumber(this.customerServiceNumber)
                .build();
    }

    public SellerBusinessInfo toSellerBusinessInfoEntity(
            Long sellerId,
            Long copyBusinessRegistrationCertificateId,
            Long copyMainOrderBusinessReportCardId
    ) {
        return SellerBusinessInfo.builder()
                .sellerId(sellerId)
                .representativeName(this.representativeName)
                .representativeContact(this.representativeContact)
                .businessName(this.businessName)
                .businessRegistrationNumber(this.businessRegistrationNumber)
                .businessCondition(this.businessCondition)
                .businessAddress(this.businessAddress)
                .businessEmail(this.businessEmail)
                .copyBusinessRegistrationCertificateId(copyBusinessRegistrationCertificateId)
                .copyMainOrderBusinessReportCardId(copyMainOrderBusinessReportCardId)
                .build();
    }

    public SellerAdjustmentInfo toSellerAdjustmentInfo(
            Long sellerId,
            Long copyBankBookId
    ) {
        return SellerAdjustmentInfo.builder()
                .sellerId(sellerId)
                .bankCode(this.bankCode)
                .accountName(this.accountName)
                .accountNumber(this.accountNumber)
                .copyBankBookId(copyBankBookId)
                .build();
    }

}
