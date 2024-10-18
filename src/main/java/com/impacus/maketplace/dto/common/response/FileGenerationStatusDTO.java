package com.impacus.maketplace.dto.common.response;

import com.impacus.maketplace.common.enumType.FileStatus;
import com.impacus.maketplace.redis.entity.FileGenerationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileGenerationStatusDTO {
    private String id;
    private FileStatus status;
    private String savedURL;

    public static FileGenerationStatusDTO toDTO(FileGenerationStatus fileGenerationStatus) {
        return new FileGenerationStatusDTO(
                fileGenerationStatus.getId(),
                fileGenerationStatus.getStatus(),
                fileGenerationStatus.getSaveURL()
        );
    }
}
