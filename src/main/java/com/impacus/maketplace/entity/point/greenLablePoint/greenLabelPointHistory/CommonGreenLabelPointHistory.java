package com.impacus.maketplace.entity.point.greenLablePoint.greenLabelPointHistory;

import com.impacus.maketplace.dto.point.CreateGreenLabelHistoryDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("COMMON")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonGreenLabelPointHistory extends GreenLabelPointHistory {

    public CommonGreenLabelPointHistory(
            CreateGreenLabelHistoryDTO dto
    ) {
        super(dto);
    }

    public static CommonGreenLabelPointHistory of(
            CreateGreenLabelHistoryDTO dto
    ) {
        return new CommonGreenLabelPointHistory(dto);
    }
}
