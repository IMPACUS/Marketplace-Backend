package com.impacus.maketplace.dto.seller.response;

import lombok.Data;

import java.util.List;

@Data
public class SellerEntryListDTO {
    private Long todayEntryCnt;
    private Long thisWeekEntryCnt;
    private Long approveEntryCnt;
    private Long rejectEntryCnt;
    private List<SimpleSellerEntryDTO> sellers;
}
