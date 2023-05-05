package com.github.microtweak.validator.conditional.core.constraint;

import com.github.microtweak.validator.conditional.core.ValidateAs;

import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional version of constraint {@link NotEmpty @NotEmpty}
 */
@ValidateAs(NotEmpty.class)
@Repeatable(NotEmptyWhen.List.class)
@Documented
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface NotEmptyWhen {

    String expression();

    String message() default "{javax.validation.constraints.NotEmpty.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    /**
     * Conditional version of constraint {@link NotEmpty.List @NotEmpty.List}
     */
    @Documented
    @Target({ METHOD, FIELD })
    @Retention(RUNTIME)
    @interface List {
        NotEmptyWhen[] value();
    }

}

