package com.impacus.maketplace.dto.coupon.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponIdListDTO {

    @NotEmpty
    private List<Long> couponIdList;
}
