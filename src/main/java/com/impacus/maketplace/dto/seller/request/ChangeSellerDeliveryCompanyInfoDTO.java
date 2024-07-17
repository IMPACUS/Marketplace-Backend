package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.enumType.DeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.SelectedSellerDeliveryCompany;
import com.impacus.maketplace.entity.seller.deliveryCompany.SellerDeliveryCompany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ChangeSellerDeliveryCompanyInfoDTO {
    @NotNull
    @Min(1)
    private int generalDeliveryFee;

    @NotNull
    @Min(1)
    private int generalSpecialDeliveryFee;

    @NotNull
    @Min(1)
    private int refundDeliveryFee;

    @NotNull
    @Min(1)
    private int refundSpecialDeliveryFee;

    @NotNull
    @Size(min = 1)
    private List<String> deliveryCompany;

    public List<DeliveryCompany> getDeliveryCompanies() {
        List<DeliveryCompany> companies = new ArrayList<>();
        for (String name : deliveryCompany) {
            companies.add(DeliveryCompany.fromName(name));
        }
        return companies;
    }

    public SellerDeliveryCompany toEntity(Long sellerId) {
        return SellerDeliveryCompany.builder()
                .sellerId(sellerId)
                .generalDeliveryFee(this.generalDeliveryFee)
                .generalSpecialDeliveryFee(this.generalSpecialDeliveryFee)
                .refundDeliveryFee(this.refundDeliveryFee)
                .refundSpecialDeliveryFee(this.refundSpecialDeliveryFee)
                .build();
    }

    public List<SelectedSellerDeliveryCompany> toSelectedEntity(Long sellerDeliveryCompanyId) {
        List<DeliveryCompany> deliveryCompanies = getDeliveryCompanies();
        List<SelectedSellerDeliveryCompany> companies = new ArrayList<>();

        for (int i = 0; i < deliveryCompanies.size(); i++) {
            SelectedSellerDeliveryCompany company = new SelectedSellerDeliveryCompany(
                    sellerDeliveryCompanyId, deliveryCompanies.get(i), i
            );
            companies.add(company);
        }

        return companies;
    }
}
