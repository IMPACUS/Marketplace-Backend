package com.impacus.maketplace.dto.category.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubCategoryDetailDTO {
    private Long subCategoryId;
    private String subCategoryName;
    private String thumbnailUrl;

    @QueryProjection
    public SubCategoryDetailDTO(Long subCategoryId,
                                String subCategoryName,
                                String thumbnailUrl) {
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
        this.thumbnailUrl = thumbnailUrl;
    }
}
