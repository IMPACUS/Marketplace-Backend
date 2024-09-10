package com.impacus.maketplace.common.validator;

import com.impacus.maketplace.common.annotation.ValidAccountNumber;
import com.impacus.maketplace.common.constants.RegExpPatternConstants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountNumberValidator implements ConstraintValidator<ValidAccountNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.matches(RegExpPatternConstants.ACCOUNT_NUMBER_PATTERN);
    }
}
