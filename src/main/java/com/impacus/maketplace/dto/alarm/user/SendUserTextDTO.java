package com.impacus.maketplace.dto.alarm.user;

import com.impacus.maketplace.common.enumType.alarm.AlarmUserCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmUserSubcategoryEnum;
import com.impacus.maketplace.common.utils.AmountComma;
import com.impacus.maketplace.dto.alarm.common.SendTextDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendUserTextDTO extends SendTextDTO {
    private AlarmUserCategoryEnum category;
    private AlarmUserSubcategoryEnum subcategory;
    private String name; // 유저명
    private String orderDate; // 주문일(yyyy-MM-dd)
    private String orderNum; // 주문번호
    private String itemName; // 상품명
    private String amount; // 주문금액
    private String courier; // 택배사
    private String invoice; // 송장번호
    private String couponName; // 쿠폰명
    private String couponAmount; // 쿠폰금액
    private String couponExpired; // 쿠폰 유효기간
    private String couponLink; // 쿠폰함 링크
    private String pointAmount; // 적립금
    private String pointExpired; // 적립금 유효기간
    private String pointLink; // 적립금 링크

    public void setAmount(int amount) {
        this.amount = AmountComma.formatCurrency(amount);
    }

    public void setCouponAmount(int couponAmount) {
        this.couponAmount = AmountComma.formatCurrency(couponAmount);
    }

    public void setPointAmount(int pointAmount) {
        this.pointAmount = AmountComma.formatCurrency(pointAmount);
    }
}
