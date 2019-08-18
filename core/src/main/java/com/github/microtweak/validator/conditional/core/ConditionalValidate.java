package com.github.microtweak.validator.conditional.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ConditionalConstraintValidator.class)
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConditionalValidate {

    @Deprecated
    String message() default "";

    @Deprecated
    Class<?>[] groups() default { };

    @Deprecated
    Class<? extends Payload>[] payload() default { };

}

