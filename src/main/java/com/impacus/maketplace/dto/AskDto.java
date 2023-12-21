package com.impacus.maketplace.dto;

import com.impacus.maketplace.common.enumType.AskType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AskDto {

    private Long id;
    private Long parentAskId;
    private String askType;
    private String title;
    private String contents;
    private boolean isAnswered;
    private boolean isDeleted;

    private String registerId;
}
