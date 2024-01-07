package com.impacus.maketplace.dto.payment.response;

import com.impacus.maketplace.entity.user.User;
import lombok.Builder;


@Builder
public record PaymentDTO() {



    public PaymentDTO(User user) {
        this();
    }
}
