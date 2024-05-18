package com.impacus.maketplace.common.constants;

import lombok.Getter;

@Getter
public class RegExpPatternConstants {
    public static final String PHONE_NUMBER_PATTEN = "^01(?:0|1|[6-9])-[1-9](?:\\d{2}|\\d{3})-\\d{4}$";

    public static final String CARD_NUMBER_PATTEN = "^\\d{4}-\\d{2}-\\d{6}$";

    public static final String CVC_PATTEN = "^\\d{3,4}$";

    public static final String CARD_EXPIRED_DATE_PATTEN = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$";
}
