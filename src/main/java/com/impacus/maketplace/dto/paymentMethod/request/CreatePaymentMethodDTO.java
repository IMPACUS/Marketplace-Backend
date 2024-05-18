package com.impacus.maketplace.dto.paymentMethod.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import com.impacus.maketplace.common.enumType.CreditCardCompany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentMethodDTO {
    @ValidEnum(enumClass = CreditCardCompany.class)
    private CreditCardCompany creditCardCompany;

    @NotBlank
    private String cardOwner;

    @NotBlank
    @Pattern(regexp = RegExpPatternConstants.CARD_NUMBER_PATTEN, message = "카드 번호의 형식이 유효하지 않습니다.")
    private String cardNumber;

    @NotBlank
    @Pattern(regexp = RegExpPatternConstants.CVC_PATTEN, message = "cvc의 형식이 유효하지 않습니다.")
    private String cvc;

    @NotBlank
    @Pattern(regexp = RegExpPatternConstants.CARD_EXPIRED_DATE_PATTEN, message = "만효일의 형식이 유효하지 않습니다.")
    private String expirationDate;
}
