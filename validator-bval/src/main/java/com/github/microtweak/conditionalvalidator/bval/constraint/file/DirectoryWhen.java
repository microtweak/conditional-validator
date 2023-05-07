package com.github.microtweak.conditionalvalidator.bval.constraint.file;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.apache.bval.extras.constraints.file.Directory;

import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Directory @Directory}
 */
@ValidateAs(Directory.class)
@Documented
@Target({ FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface DirectoryWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.file.Directory.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
