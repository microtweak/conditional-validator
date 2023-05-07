package com.github.microtweak.conditionalvalidator.internal.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FakeConstraintStringValidator implements ConstraintValidator<FakeConstraint, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }

}
