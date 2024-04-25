package com.impacus.maketplace.dto.seller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SellerEntryStatusDTO {
    private Long todayEntryCnt;
    private Long thisWeekEntryCnt;
    private Long approveEntryCnt;
    private Long rejectEntryCnt;
}
