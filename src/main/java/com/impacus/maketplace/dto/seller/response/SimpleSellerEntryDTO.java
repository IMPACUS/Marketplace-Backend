package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import com.impacus.maketplace.common.utils.StringUtils;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SimpleSellerEntryDTO {
    private Long id;
    private LocalDateTime requestAt;
    private String marketName;
    private String contactNumber;
    private String businessCondition;
    private EntryStatus entryStatus;

    @QueryProjection
    public SimpleSellerEntryDTO(
            Long id,
            LocalDateTime requestAt,
            String marketName,
            String phoneNumberPrefix,
            String phoneNumberSuffix,
            String businessCondition,
            EntryStatus entryStatus
    ) {
        this.id = id;
        this.requestAt = requestAt;
        this.marketName = marketName;
        this.contactNumber = StringUtils.getPhoneNumber(phoneNumberPrefix, phoneNumberSuffix);
        this.businessCondition = businessCondition;
        this.entryStatus = entryStatus;
    }
}
