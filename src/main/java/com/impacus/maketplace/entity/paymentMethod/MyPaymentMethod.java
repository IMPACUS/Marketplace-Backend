package com.impacus.maketplace.entity.paymentMethod;

import com.impacus.maketplace.common.BaseEntity;
import com.impacus.maketplace.common.converter.AES256ToStringConverter;
import com.impacus.maketplace.common.enumType.CreditCardCompany;
import jakarta.persistence.*;
import lombok.*;

//@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "my_payment_method_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Convert(converter = AES256ToStringConverter.class)
    private String cardNumber;

    @Column(nullable = false)
    private String customerUid; // 빌링키

    private CreditCardCompany creditCardCompany;
}
