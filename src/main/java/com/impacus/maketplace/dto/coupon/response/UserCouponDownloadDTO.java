package com.impacus.maketplace.dto.coupon.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCouponDownloadDTO {
    private Long userCouponId;
    private Boolean isDownload;
}
