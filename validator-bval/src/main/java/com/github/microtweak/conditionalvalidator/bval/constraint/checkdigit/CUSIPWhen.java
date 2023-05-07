package com.github.microtweak.conditionalvalidator.bval.constraint.checkdigit;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.apache.bval.extras.constraints.checkdigit.CUSIP;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Conditional version of constraint {@link CUSIP @CUSIP}
 */
@ValidateAs(CUSIP.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface CUSIPWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.checkdigit.CUSIP.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
