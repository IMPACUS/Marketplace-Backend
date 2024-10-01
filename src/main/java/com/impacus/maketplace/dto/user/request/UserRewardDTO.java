package com.impacus.maketplace.dto.user.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UserRewardDTO {

    @Min(1)
    private Long levelPoint;

    @Min(1)
    private Long greenLabelPoint;

    private Long couponId;
}
