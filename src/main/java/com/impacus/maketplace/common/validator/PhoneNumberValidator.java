package com.impacus.maketplace.common.validator;

import com.impacus.maketplace.common.annotation.ValidPhoneNumber;
import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches(RegExpPatternConstants.PHONE_NUMBER_PATTEN);
    }
}
