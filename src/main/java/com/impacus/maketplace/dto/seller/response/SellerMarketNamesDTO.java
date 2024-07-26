package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.repository.seller.mapping.SellerMarketNameViewsMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class SellerMarketNamesDTO {
    private Long sellerId;

    private String marketName;

    public static SellerMarketNamesDTO from(SellerMarketNameViewsMapping mapping) {
        return new SellerMarketNamesDTO(mapping.getId(), mapping.getMarketName());
    }
}
