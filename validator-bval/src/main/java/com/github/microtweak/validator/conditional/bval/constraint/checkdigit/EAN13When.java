package com.github.microtweak.validator.conditional.bval.constraint.checkdigit;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.apache.bval.extras.constraints.checkdigit.EAN13;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@WhenActivatedValidateAs(EAN13.class)
@Documented
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface EAN13When {

    String message() default "{org.apache.bval.extras.constraints.checkdigit.EAN13.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
