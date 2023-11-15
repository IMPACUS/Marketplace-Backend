package com.impacus.maketplace.common.utils;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
        "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~])[a-zA-Z0-9!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~]{13,16}$");

    public static Boolean checkPasswordValidation(String password) {
        return PASSWORD_PATTERN.matcher(password).find();
    }

}
