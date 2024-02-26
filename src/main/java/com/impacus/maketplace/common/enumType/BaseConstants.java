package com.impacus.maketplace.common.enumType;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class BaseConstants {

    public static final String BOOLEAN_TRUE  = "true";
    public static final String BOOLEAN_FALSE = "false";

    public static final String RESPONSE_SUCCESS    = "S";
    public static final String RESPONSE_SUCCESS_MSG = "Success";
    public static final String RESPONSE_ERROR      = "E";
    public static final String RESPONSE_ERROR_MSG  = "Error";
    public static final String RESPONSE_FAIL       = "F";
    public static final String RESPONSE_FAIL_MSG   = "Fail";

    public static final String Y = "Y";
    public static final String N = "N";

    public static final String ZERO = "0";
    public static final String ONE  = "1";

    public static final int I_ZERO = 0;
    public static final int I_ONE  = 1;

    public static final String CHECK_YES   = "1";
    public static final String CHECK_NO    = "0";
    public static final String CHECK_TRUE  = "1";
    public static final String CHECK_FALSE = "0";

    public static final String CHARSET_ISO8859_1 = "ISO-8859-1";
    public static final String CHARSET_UTF8      = "UTF-8";
    public static final String DEFAULT_CHARSET   = "UTF-8";
    public static final String COMMA             = ",";
    public static final String PERIOD            = ".";
    public static final String EMPTY             = "";
    public static final String SPACE             = " ";
    public static final String SLASH             = "/";
    public static final String SEMICOLON         = ";";
    public static final String COLON             = ":";
    public static final String UNDERBAR          = "_";
    public static final String HYPHEN            = "-";
    public static final String ASTERISK          = "*";
    public static final String PIPE              = "|";
    public static final String DOUBLE_BACK_SLASH = "\\";
    public static final String CARRIAGE_RETURN   = "\n";
    public static final String DOUBLE_QUOTE      = "\"";
    public static final String SINGLE_QUOTE      = "'";
    public static final String HTML_SPACE        = "&nbsp;";
    public static final String LINE_BREAK        = "<br>";

    public static final long FILE_SIZE_KB = 1024L;
    public static final long FILE_SIZE_MB = 1024L * 1024;
    public static final long FILE_SIZE_GB = 1024L * 1024 * 1024;

    public static final int TIME_UNIT_MILLIS = 1000;
    public static final int TIME_UNIT_MINUTE = 60;
    public static final int TIME_UNIT_HOUR   = 60 * TIME_UNIT_MINUTE;
    public static final int TIME_UNIT_DAY    = 24 * TIME_UNIT_HOUR;
    public static final int TIME_UNIT_WEEK   = 7 * TIME_UNIT_DAY;

    public static final String HTTP  = "http";
    public static final String HTTPS = "https";

    public final static String DATE_FORMAT      = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:ms:ss";
    public final static String DATE_FORMAT_INT  = "yyyyMMdd";

    public LocalDateTime stringToLocalDateTime(String time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(time, dtf);
    }
}
