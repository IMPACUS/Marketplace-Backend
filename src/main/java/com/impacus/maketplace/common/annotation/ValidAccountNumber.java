package com.impacus.maketplace.common.annotation;

import com.impacus.maketplace.common.validator.AccountNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AccountNumberValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccountNumber {
    String message() default "Invalid date of account number format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
