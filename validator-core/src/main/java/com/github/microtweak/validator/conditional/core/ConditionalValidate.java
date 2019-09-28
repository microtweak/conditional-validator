package com.github.microtweak.validator.conditional.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DelegatedConditionalConstraintValidator.class)
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConditionalValidate {

    Class<?>[] contextClasses() default {};

    /**
     * @deprecated Ignored/not used by ConditionalValidation. But Bean Validation requires that every constraint has the "message" attribute.
     */
    @Deprecated
    String message() default "";

    /**
     * @deprecated Ignored/not used by ConditionalValidation. But Bean Validation requires that every constraint has the "groups" attribute.
     */
    @Deprecated
    Class<?>[] groups() default { };

    /**
     * @deprecated Ignored/not used by ConditionalValidation. But Bean Validation requires that every constraint has the "payload" attribute.
     */
    @Deprecated
    Class<? extends Payload>[] payload() default { };

}

