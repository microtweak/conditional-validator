package com.github.microtweak.validator.conditional.bval.constraint.creditcard;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.apache.bval.extras.constraints.creditcard.Visa;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(Visa.class)
@Documented
@Target({ FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface VisaWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.creditcard.Visa.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
