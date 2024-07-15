package com.impacus.maketplace.common.annotation;

import com.impacus.maketplace.common.validator.PhoneNumberValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface ValidPhoneNumber {
    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};
}
