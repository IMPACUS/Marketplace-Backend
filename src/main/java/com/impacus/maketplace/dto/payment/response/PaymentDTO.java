package com.impacus.maketplace.dto.payment.response;

import com.impacus.maketplace.entity.user.User;
import com.impacus.maketplace.vo.auth.TokenInfoVO;
import lombok.Builder;


@Builder
public record PaymentDTO() {



    public PaymentDTO(User user) {
        this();
    }
}
