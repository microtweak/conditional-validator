package com.github.microtweak.conditionalvalidator.internal.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FakeConstraintCharSequenceValidator implements ConstraintValidator<FakeConstraint, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }

}
