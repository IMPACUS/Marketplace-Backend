package com.impacus.maketplace.dto.payment.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckoutSingleDTO {
    @NotNull(message = "결제 상품 정보는 필수 요청 데이터입니다.")
    private PaymentProductInfoDTO paymentProductInfo;   // 결제 상품 정보
    @NotNull(message = "주소 정보는 필수 요청 데이터입니다.")
    private AddressInfoDTO addressInfoDTO;  // 결제시 입력한 주소지
    private List<Long> appliedOrderCouponIds = new ArrayList<>();  // 전체 주문에 적용된 사용자 쿠폰 리스트
    @NotNull(message = "포인트를 사용하지 않을 경우 포인트 금액의 기본 값을 0으로 설정해주세요.")
    @Min(value = 0L, message = "사용한 포인트는 음수가 될 수 없습니다.")
    private Long pointAmount = 0L;   // 사용한 포인트 금액
    @ValidEnum(enumClass = PaymentMethod.class)
    private PaymentMethod method;   // 결제 방식
    private Boolean usedRegisteredCard = false; // 등록된 카드 사용 유무
    private Long registeredCardId;  // 사용한 카드의 id
    private Long calculatedTotalAmount;  // 프론트 서버에서 계산한 금액

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setAppliedOrderCouponIds(List<Long> appliedOrderCouponIds) {
        this.appliedOrderCouponIds = appliedOrderCouponIds != null ? appliedOrderCouponIds : Collections.emptyList();
    }
}
