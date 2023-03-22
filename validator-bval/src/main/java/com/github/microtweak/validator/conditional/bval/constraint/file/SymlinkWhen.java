package com.github.microtweak.validator.conditional.bval.constraint.file;

import com.github.microtweak.validator.conditional.core.WhenActivatedValidateAs;
import org.apache.bval.extras.constraints.file.Symlink;

import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link Symlink @Symlink}
 */
@WhenActivatedValidateAs(Symlink.class)
@Documented
@Target({ FIELD, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
public @interface SymlinkWhen {

    String expression();

    String message() default "{org.apache.bval.extras.constraints.file.Symlink.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
