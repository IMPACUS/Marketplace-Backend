package com.impacus.maketplace.common.converter;

import com.impacus.maketplace.common.enumType.error.ErrorType;
import com.impacus.maketplace.common.exception.CustomException;
import com.impacus.maketplace.common.utils.cryptography.AES256Utils;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.util.StringUtils;

@Converter
public class AES256ToStringConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (!StringUtils.hasText(attribute)) {
            return attribute;
        }
        try {
            return AES256Utils.encryptAES256(attribute);
        } catch (Exception e) {
            throw new CustomException(ErrorType.ENCRYPTION_FAILED, e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null) {
                return null;
            }
            return AES256Utils.decryptAES256(dbData);
        } catch (Exception e) {
            throw new CustomException(ErrorType.DECRYPTION_FAILED, e);
        }
    }
}
