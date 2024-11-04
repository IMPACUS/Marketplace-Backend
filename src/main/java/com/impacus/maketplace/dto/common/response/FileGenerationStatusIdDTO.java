package com.impacus.maketplace.dto.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileGenerationStatusIdDTO {
    private String id;

    public static FileGenerationStatusIdDTO toDTO(String fileGenerationStatusId) {
        return new FileGenerationStatusIdDTO(fileGenerationStatusId);
    }
}
