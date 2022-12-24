package com.github.microtweak.validator.conditional.internal.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
    FakeConstraintStringValidator.class,
    FakeConstraintCharSequenceValidator.class
})
public @interface FakeConstraint {

    String message() default "It is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}