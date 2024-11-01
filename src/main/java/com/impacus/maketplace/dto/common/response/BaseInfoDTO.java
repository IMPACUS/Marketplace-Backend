package com.impacus.maketplace.dto.common.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BaseInfoDTO {
    @NotNull
    private BaseInfoDetailDTO greenTagCoupon;

    @NotNull
    private BaseInfoDetailDTO userCoupon;

    @NotNull
    private BaseInfoDetailDTO snsTagCoupon;

    @NotNull
    private BaseInfoDetailDTO pointReward;

    @NotNull
    private BaseInfoDetailDTO levelPoint;

    private LocalDateTime modifyAt;

    public BaseInfoDTO(List<BaseInfoDetailDTO> baseInfoDetailDTOS) {
        for (BaseInfoDetailDTO baseInfoDetailDTO : baseInfoDetailDTOS) {
            switch (baseInfoDetailDTO.getInfoType()) {
                case GREEN_TAG_COUPON -> {
                    this.greenTagCoupon = baseInfoDetailDTO;
                }
                case USER_COUPON -> {
                    this.userCoupon = baseInfoDetailDTO;
                }
                case SNS_TAG_COUPON -> {
                    this.snsTagCoupon = baseInfoDetailDTO;
                }
                case POINT_REWARD -> {
                    this.pointReward = baseInfoDetailDTO;
                }
                case LEVEL_POINT -> {
                    this.levelPoint = baseInfoDetailDTO;
                }
            }
        }
    }
}
