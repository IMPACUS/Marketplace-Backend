package com.impacus.maketplace.dto.coupon.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponUserSearchDto {

    private Long userId;
    @NotNull
    private String searchType;
    private String searchValue;
    private String sortType;
}
