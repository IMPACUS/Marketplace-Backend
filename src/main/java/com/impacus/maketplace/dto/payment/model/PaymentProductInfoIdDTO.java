package com.impacus.maketplace.dto.payment.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentProductInfoIdDTO {
    Long productId;
    Long productOptionId;
    Long sellerId;
}
