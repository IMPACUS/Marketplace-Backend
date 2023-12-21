package com.impacus.maketplace.common.enumType.category;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubCategory {


    TSHRT(1, SuperCategory.FASHION, "T-shirt"),
    PANT(2, SuperCategory.FASHION, "바지"),
    SHIRT(3, SuperCategory.FASHION, "셔츠"),
    SHOE(4, SuperCategory.FASHION, "신발"),
    HAT(5, SuperCategory.FASHION, "모자"),
    JACKET(6, SuperCategory.FASHION, "자켓"),
    COAT(7, SuperCategory.FASHION, "코트"),
    JUMPER(8, SuperCategory.FASHION, "잠바"),
    SCARF_MUFFLER(9, SuperCategory.FASHION, "스카프/머플러"),
    KNIT_CARDIGAN(10, SuperCategory.FASHION, "니트/가디건"),
    BEACHWEAR(11, SuperCategory.FASHION, "비치웨어"),
    GLOVES(12, SuperCategory.FASHION, "장갑"),
    BELT(13, SuperCategory.FASHION, "벨트"),
    SOCKS(14, SuperCategory.FASHION, "양말"),
    STOCKING(15, SuperCategory.FASHION, "스타킹");

    private final int code;
    private final SuperCategory superCategory;
    private final String value;
}
