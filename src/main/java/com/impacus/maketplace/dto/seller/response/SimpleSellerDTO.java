package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.entity.seller.Seller;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SimpleSellerDTO {
    private Long id;
    private String contactName;
    private EntryStatus entryStatus;

    public SimpleSellerDTO(Long userId, Seller seller) {
        this.id = userId;
        this.contactName = seller.getContactName();
        this.entryStatus = seller.getEntryStatus();
    }

    public static SimpleSellerDTO toDTO(Long userId, Seller seller) {
        return new SimpleSellerDTO(userId, seller);
    }
}
