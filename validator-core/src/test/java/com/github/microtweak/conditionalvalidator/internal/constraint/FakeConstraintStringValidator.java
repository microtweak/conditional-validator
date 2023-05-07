package com.github.microtweak.conditionalvalidator.internal.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FakeConstraintStringValidator implements ConstraintValidator<FakeConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }

}
