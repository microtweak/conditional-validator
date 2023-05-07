package com.github.microtweak.conditionalvalidator.bval.constraint.file;

import com.github.microtweak.conditionalvalidator.ValidateAs;
import org.apache.bval.extras.constraints.file.NotDirectory;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link NotDirectory @NotDirectory}
 */
@ValidateAs(NotDirectory.class)
@Documented
@Target({ FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface NotDirectoryWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.file.NotDirectory.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
