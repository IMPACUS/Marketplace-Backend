package com.impacus.maketplace.dto.common.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachFileDTO {
    private Long id;
    private String fileURL;

    @QueryProjection
    public AttachFileDTO(Long id, String fileURL) {
        this.id = id;
        this.fileURL = fileURL;
    }
}
