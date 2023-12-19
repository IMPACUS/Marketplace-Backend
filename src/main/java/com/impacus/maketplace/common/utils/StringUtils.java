package com.impacus.maketplace.common.utils;

import com.impacus.maketplace.common.enumType.OauthProviderType;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

    public static Boolean checkPasswordValidation(String password) {
        return password.matches(PASSWORD_PATTERN);
    }

    public static String createStrEmail(String email, OauthProviderType oauthProviderType) {
        return oauthProviderType + "_" + email;
    }

    public static String parseGrantTypeInToken(String strGrantType, String token) {
        if (token.startsWith(strGrantType + " ")) {
            return token.substring(strGrantType.length() + 1);
        }

        return token;
    }

    /**
     * 파일명에서 파일 확장자를 찾는 함수
     *
     * @param filename
     * @return
     */
    public static Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
