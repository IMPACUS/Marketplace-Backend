package com.impacus.maketplace.dto.seller.request;

import com.impacus.maketplace.common.annotation.ValidEnum;
import com.impacus.maketplace.common.enumType.seller.EntryStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSellerEntryStatusDTO {

    @NotNull
    private Long userId;

    @ValidEnum(enumClass = EntryStatus.class)
    private EntryStatus entryStatus;

    @Min(0)
    @Max(100)
    private Integer charge;
}
