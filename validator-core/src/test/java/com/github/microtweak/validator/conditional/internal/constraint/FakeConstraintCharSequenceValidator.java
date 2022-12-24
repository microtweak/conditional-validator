package com.github.microtweak.validator.conditional.internal.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FakeConstraintCharSequenceValidator implements ConstraintValidator<FakeConstraint, CharSequence> {

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }

}
