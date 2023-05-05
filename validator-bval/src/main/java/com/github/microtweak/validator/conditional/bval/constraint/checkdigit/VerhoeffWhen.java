package com.github.microtweak.validator.conditional.bval.constraint.checkdigit;

import com.github.microtweak.validator.conditional.core.ValidateAs;
import org.apache.bval.extras.constraints.checkdigit.Verhoeff;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Verhoeff @Verhoeff}
 */
@ValidateAs(Verhoeff.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface VerhoeffWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.checkdigit.Verhoeff.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
