package com.impacus.maketplace.dto.payment.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.payment.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CheckoutCartDTO {
    @NotEmpty(message = "장바구니 id 리스트는 빈 값이 올 수 없습니다.")
    private List<Long> shoppingBasketIdList;    // 장바구니 id List
    @NotEmpty(message = "결제 상품 정보로 빈 값이 올 수 없습니다.")
    private List<PaymentProductInfoDTO> paymentProductInfos = new ArrayList<>();   // 결제 상품 정보
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

    @JsonSetter(nulls = Nulls.AS_EMPTY) // JSON 데이터에서 null이 들어올 경우 빈 리스트로 초기화
    public void setPaymentProductInfos(List<PaymentProductInfoDTO> paymentProductInfos) {
        this.paymentProductInfos = paymentProductInfos != null ? paymentProductInfos : new ArrayList<>();
    }

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    public void setAppliedOrderCouponIds(List<Long> appliedOrderCouponIds) {
        this.appliedOrderCouponIds = appliedOrderCouponIds != null ? appliedOrderCouponIds : new ArrayList<>();
    }
}
