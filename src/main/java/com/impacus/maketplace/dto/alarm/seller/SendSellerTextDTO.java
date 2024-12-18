package com.impacus.maketplace.dto.alarm.seller;

import com.impacus.maketplace.common.enumType.alarm.AlarmSellerCategoryEnum;
import com.impacus.maketplace.common.enumType.alarm.AlarmSellerSubcategoryEnum;
import com.impacus.maketplace.common.utils.AmountComma;
import com.impacus.maketplace.dto.alarm.common.SendTextDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendSellerTextDTO extends SendTextDTO {
    private AlarmSellerCategoryEnum category;
    private AlarmSellerSubcategoryEnum subcategory;
    private String brand; // 브랜드명
    private String orderDate; // 주문일(yyyy-MM-dd)
    private String orderNum; // 주문번호
    private String itemName; // 상품명
    private String amount; // 주문금액
    private String invoice; // 운송장번호

    public void setAmount(int amount) {
        this.amount = AmountComma.formatCurrency(amount);
    }
}
