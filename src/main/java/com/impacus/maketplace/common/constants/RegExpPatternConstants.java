package com.impacus.maketplace.common.constants;

import lombok.Getter;

@Getter
public class RegExpPatternConstants {
    public static final String PHONE_NUMBER_PATTERN = "^01(?:0|1|[6-9])-[1-9](?:\\d{2}|\\d{3})-\\d{4}$";

    public static final String CARD_NUMBER_PATTERN = "^\\d{4}-\\d{2}-\\d{6}$";

    public static final String CVC_PATTERN = "^\\d{3,4}$";

    public static final String CARD_EXPIRED_DATE_PATTERN = "^(0[1-9]|1[0-2])\\/?([0-9]{2})$";

    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

    public static final String ACCOUNT_NUMBER_PATTERN = "^[0-9\\\\-]+$";
}