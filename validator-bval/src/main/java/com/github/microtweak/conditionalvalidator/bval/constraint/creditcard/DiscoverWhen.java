package com.github.microtweak.conditionalvalidator.bval.constraint.creditcard;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.apache.bval.extras.constraints.creditcard.Discover;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Discover @Discover}
 */
@ValidateAs(Discover.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface DiscoverWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.creditcard.Discover.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
