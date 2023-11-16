package com.impacus.maketplace.common.utils;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

    public static Boolean checkPasswordValidation(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

}
