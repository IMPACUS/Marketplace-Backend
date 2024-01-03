package com.impacus.maketplace.dto.point;

import com.impacus.maketplace.entity.point.PointMaster;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointSettingRequestDto {

    private Long userId;
    private Integer celebrationPoint;
//    private Integer userScore;

    public PointMaster toEntity() {
        return PointMaster.builder()
                .userId(this.userId)
                .availablePoint(celebrationPoint)
                .userScore(celebrationPoint)
                .build();

    }


}
