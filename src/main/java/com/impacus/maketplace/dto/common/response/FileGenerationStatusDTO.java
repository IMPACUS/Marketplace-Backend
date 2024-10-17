package com.impacus.maketplace.dto.common.response;

import com.impacus.maketplace.common.enumType.FileStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileGenerationStatusDTO {
    private Long id;
    private FileStatus status;
    private String savedURL;
}
