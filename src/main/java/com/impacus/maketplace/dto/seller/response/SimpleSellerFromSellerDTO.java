package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.entity.seller.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleSellerFromSellerDTO {
    private Long id;
    private String contactName;
    private EntryStatus entryStatus;

    public SimpleSellerFromSellerDTO(Long userId, Seller seller) {
        this.id = userId;
        this.contactName = seller.getContactName();
        this.entryStatus = seller.getEntryStatus();
    }

    public static SimpleSellerFromSellerDTO toDTO(Long userId, Seller seller) {
        return new SimpleSellerFromSellerDTO(userId, seller);
    }
}
