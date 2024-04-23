package com.impacus.maketplace.dto.seller.response;

import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SimpleSellerEntryDTO {
    private LocalDateTime requestAt;
    private String marketName;
    private String contactNumber;
    private String businessCondition;
    private EntryStatus entryStatus;
}
