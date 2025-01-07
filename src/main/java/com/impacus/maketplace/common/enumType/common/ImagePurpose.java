package com.impacus.maketplace.common.enumType.common;

import com.impacus.maketplace.common.constants.DirectoryConstants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImagePurpose {
    EDITOR(1, DirectoryConstants.EDITOR_IMAGE_DIRECTORY),
    PRODUCT(2, DirectoryConstants.PRODUCT_IMAGE_DIRECTORY),
    REVIEW(3, DirectoryConstants.REVIEW_PRODUCT_IMAGE_DIRECTORY);

    private final int code;
    private final String directory;
}
