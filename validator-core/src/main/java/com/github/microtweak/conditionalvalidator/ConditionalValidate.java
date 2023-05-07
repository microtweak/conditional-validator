package com.github.microtweak.conditionalvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * When a class is annotated with @ConditionalValidate, all of its fields can be annotated with any conditional constraint.
 */
@Constraint(validatedBy = DelegatedConditionalConstraintValidator.class)
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConditionalValidate {

    /**
     * Enums that will be available during the evaluation of the expression of each conditional constraint
     */
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

