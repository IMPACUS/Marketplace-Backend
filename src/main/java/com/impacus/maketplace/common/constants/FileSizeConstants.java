package com.impacus.maketplace.common.constants;

import lombok.Getter;

@Getter
public class FileSizeConstants {
    public static final int THUMBNAIL_SIZE_LIMIT = 10800; // 60 픽셀 * 60픽셀

    public static final int PRODUCT_IMAGE_SIZE_LIMIT = 341172; // (1080 * 1053 * 3 = 3.41172MB 341172byte)
    public static final int PRODUCT_DESCRIPTION_IMAGE_SIZE_LIMIT = 341172;

    public static final int SELLER_FILE_LIMIT = 5242880;
    public static final int LOGO_IMAGE_LIMIT = 187500;


    // 리뷰 사용
    public static final int REVIEW_PRODUCT_FILE_LIMIT = 5242880;
}
